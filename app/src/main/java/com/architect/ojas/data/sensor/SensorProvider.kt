package com.architect.ojas.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.architect.ojas.domain.model.OjasState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs
import kotlin.math.sqrt

class SensorProvider(private val context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    private val _state = MutableStateFlow(OjasState())
    val state: StateFlow<OjasState> = _state.asStateFlow()

    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var isHighPerformanceMode = false

    fun startListening() {
        // Start in low-power mode by default to conserve battery
        registerSensors(SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    private fun registerSensors(delay: Int) {
        sensorManager.unregisterListener(this)
        accelerometer?.let { sensorManager.registerListener(this, it, delay) }
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val delta = abs(x - lastX) + abs(y - lastY) + abs(z - lastZ)
            val isDeviceMoving = delta > 0.15f

            // Dynamic Battery Throttling Logic
            if (isDeviceMoving && !isHighPerformanceMode) {
                isHighPerformanceMode = true
                registerSensors(SensorManager.SENSOR_DELAY_GAME) // Elevate to high FPS on movement
            } else if (!isDeviceMoving && isHighPerformanceMode) {
                isHighPerformanceMode = false
                registerSensors(SensorManager.SENSOR_DELAY_NORMAL) // Drop to low power when still
            }

            lastX = x
            lastY = y
            lastZ = z

            val flux = sqrt(x * x + y * y + z * z)

            _state.update {
                it.copy(
                    tiltX = x,
                    tiltY = y,
                    tiltZ = z,
                    fluxIntensity = flux,
                    isDeviceMoving = isDeviceMoving
                )
            }
        } else if (event.sensor.type == Sensor.TYPE_LIGHT) {
            _state.update { it.copy(ambientLight = event.values[0]) }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun setEcoMode(enabled: Boolean) {
        if (enabled) {
            registerSensors(SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            registerSensors(SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun updateState(transform: (OjasState) -> OjasState) {
        _state.update(transform)
    }
}
