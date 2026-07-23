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
    
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val barometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    fun getSensorFlux(): Flow<OjasState> = callbackFlow {
        var currentMagnetic = 0f
        var currentPressure = 0f
        var currentLight = 0f

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor.type) {
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            // Calculate Magnitude: sqrt(x^2 + y^2 + z^2)
                            val magnitude = sqrt(it.values[0] * it.values[0] + it.values[1] * it.values[1] + it.values[2] * it.values[2])
                            currentMagnetic = (magnitude / 100f).coerceIn(0f, 1f)
                        }
                        Sensor.TYPE_PRESSURE -> {
                            // Normalize pressure (standard sea level is ~1013 hPa)
                            currentPressure = ((it.values[0] - 950f) / 100f).coerceIn(0f, 1f)
                        }
                        Sensor.TYPE_LIGHT -> {
                            currentLight = (it.values[0] / 1000f).coerceIn(0f, 1f)
                        }
                    }
                    trySend(OjasState(
                        magneticFieldIntensity = currentMagnetic,
                        airPressureDensity = currentPressure,
                        lightLumenLevel = currentLight
                    ))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, barometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_UI)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
