package com.architect.ojas.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.architect.ojas.domain.model.OjasState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class SensorProvider(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Hardware Sensors
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    private val _state = MutableStateFlow(OjasState())
    val state: StateFlow<OjasState> = _state.asStateFlow()

    private var audioRecord: AudioRecord? = null
    private var isRecordingAudio = false

    fun startListening() {
        // Register all available hardware sensors
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        gyroscope?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        pressureSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }

        startAcousticBlowListener()
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        stopAcousticBlowListener()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val flux = sqrt(x * x + y * y + z * z)

                _state.update {
                    it.copy(
                        tiltX = x,
                        tiltY = y,
                        tiltZ = z,
                        fluxIntensity = flux
                    )
                }
            }

            Sensor.TYPE_GYROSCOPE -> {
                _state.update {
                    it.copy(
                        gyroX = event.values[0],
                        gyroY = event.values[1],
                        gyroZ = event.values[2]
                    )
                }
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                val magX = event.values[0]
                val magY = event.values[1]
                val magZ = event.values[2]
                val magTotal = sqrt(magX * magX + magY * magY + magZ * magZ)

                _state.update { it.copy(magneticField = magTotal) }
            }

            Sensor.TYPE_LIGHT -> {
                _state.update { it.copy(ambientLight = event.values[0]) }
            }

            Sensor.TYPE_PRESSURE -> {
                _state.update { it.copy(atmosphericPressure = event.values[0]) }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // Acoustic / Blow Detection via Audio Amplitude
    private fun startAcousticBlowListener() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sampleRate = 8000
                val bufferSize = AudioRecord.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                if (bufferSize <= 0) return@launch

                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize
                )

                audioRecord?.startRecording()
                isRecordingAudio = true
                val buffer = ShortArray(bufferSize)

                while (isRecordingAudio) {
                    val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (readSize > 0) {
                        var sum = 0.0
                        for (i in 0 until readSize) {
                            sum += buffer[i] * buffer[i]
                        }
                        val amplitude = sqrt(sum / readSize)
                        
                        _state.update { it.copy(soundAcousticDb = amplitude.toFloat()) }
                    }
                }
            } catch (e: Exception) {
                // Microphones may require RECORD_AUDIO runtime permissions
            }
        }
    }

    private fun stopAcousticBlowListener() {
        isRecordingAudio = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
}
