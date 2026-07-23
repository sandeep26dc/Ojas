package com.architect.ojas.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticEngine(context: Context) {
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun pulse(flux: Float, viscosity: Float) {
        if (flux < 0.1f) return
        val intensity = (flux * 255).toInt().coerceIn(0, 255)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0, 50, 100),
                intArrayOf(0, intensity, 0),
                -1
            )
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(VibrationEffect.createOneShot(20, intensity))
        }
    }
}
