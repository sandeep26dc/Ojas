package com.architect.ojas.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.sin

class ZenEngine {
    private var audioTrack: AudioTrack? = null
    private val sampleRate = 44100
    private var isRunning = false
    private var phase = 0.0

    fun start() {
        if (isRunning) return
        isRunning = true
        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .build())
            .setAudioFormat(AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(sampleRate)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build())
            .setBufferSizeInBytes(bufferSize)
            .build()
        
        audioTrack?.play()
    }

    fun updateAudio(magneticFlux: Float, pressure: Float) {
        if (!isRunning) return
        val frequency = 100.0 + (magneticFlux * 400.0) // 100Hz to 500Hz
        val amplitude = 5000 + (pressure * 10000).toInt()
        
        val buffer = ShortArray(1024)
        for (i in buffer.indices) {
            buffer[i] = (sin(phase) * amplitude).toInt().toShort()
            phase += 2.0 * Math.PI * frequency / sampleRate
        }
        audioTrack?.write(buffer, 0, buffer.size)
    }

    fun stop() {
        isRunning = false
        audioTrack?.stop()
        audioTrack?.release()
    }
}
