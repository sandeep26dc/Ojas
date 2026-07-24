package com.architect.ojas.domain.model

data class OjasState(
    val tiltX: Float = 0f,
    val tiltY: Float = 0f,
    val tiltZ: Float = 0f,
    val ambientLight: Float = 0f,
    val fluxIntensity: Float = 0f,
    val isFluidMode: Boolean = true,
    val audioVolume: Float = 0.5f,
    val sensorSensitivity: Float = 1.0f,
    val isEcoMode: Boolean = true,
    val isDeviceMoving: Boolean = false,
    val soundAcousticDb: Float = 0f
) {
    // Legacy aliases to preserve compatibility across existing UI components
    val magneticFieldIntensity: Float get() = fluxIntensity
    val airPressureDensity: Float get() = tiltZ
    val lightLumenLevel: Float get() = ambientLight
    val isSystemStable: Boolean get() = !isDeviceMoving
    val gForceVector: FloatArray get() = floatArrayOf(tiltX, tiltY, tiltZ)
}
