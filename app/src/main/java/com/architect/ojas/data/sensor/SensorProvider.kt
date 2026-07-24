package com.architect.ojas.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.architect.ojas.domain.model.OjasState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.sqrt

class SensorProvider(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    
    // Check for existence safely
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val barometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    fun getSensorFlux(): Flow<OjasState> = callbackFlow {
        var currentMagnetic = 0f
        var currentPressure = 0f
        var currentLight = 0.5f // Default to neutral light
        var gX = 0f; var gY = 0f; var gZ = 0f

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor.type) {
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            val mag = sqrt(it.values[0]*it.values[0] + it.values[1]*it.values[1] + it.values[2]*it.values[2])
                            currentMagnetic = (mag / 100f).coerceIn(0f, 1f)
                        }
                        Sensor.TYPE_PRESSURE -> {
                            currentPressure = ((it.values[0] - 950f) / 100f).coerceIn(0f, 1f)
                        }
                        Sensor.TYPE_LIGHT -> {
                            currentLight = (it.values[0] / 1000f).coerceIn(0f, 1f)
                        }
                        Sensor.TYPE_ACCELEROMETER -> {
                            gX = it.values[0]; gY = it.values[1]; gZ = it.values[2]
                        }
                    }
                    trySend(OjasState(
                        magneticFieldIntensity = currentMagnetic,
                        airPressureDensity = currentPressure,
                        lightLumenLevel = currentLight,
                        gForceVector = Triple(gX, gY, gZ)
                    ))
                }
            }
            override fun onAccuracyChanged(s: Sensor?, a: Int) {}
        }

        // ONLY register if the hardware actually exists
        magnetometer?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        barometer?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        lightSensor?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        accelerometer?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
