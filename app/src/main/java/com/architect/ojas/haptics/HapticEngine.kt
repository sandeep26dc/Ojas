package com.architect.ojas.haptics

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticEngine(context: Context) {
    private val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_31) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    /**
     * Creates a 'Magnetic Thrum' - a subtle, low-frequency pulse 
     * that scales with the intensity of the magnetic field.
     */
    fun pulse(flux: Float, viscosity: Float) {
        if (flux < 0.1f) return // Stealth mode when flux is low

        // Map viscosity to 'sharpness'
        // High viscosity (Lead) = Low frequency, heavy thud
        // Low viscosity (Water) = High frequency, sharp tick
        val intensity = (flux * 255).toInt().coerceIn(0, 255)
        
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            // Advanced Envelope: A 'breathing' pulse
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0, 50, 100), // Timing: Start, Peak, Fade
                intArrayOf(0, intensity, 0), // Amplitudes
                -1
            )
            vibrator.vibrate(effect)
        } else {
            // Fallback for older high-end hardware
            vibrator.vibrate(VibrationEffect.createOneShot(20, intensity))
        }
    }
}
