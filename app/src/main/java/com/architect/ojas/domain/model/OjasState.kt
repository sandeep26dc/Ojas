package com.architect.ojas.domain.model

data class OjasState(
    val tiltX: Float = 0f,
    val tiltY: Float = 0f,
    val tiltZ: Float = 0f,
    val ambientLight: Float = 0f,
    val fluxIntensity: Float = 0f,
    val isFluidMode: Boolean = true,
    val audioVolume: Float = 0.5f,
    val isEcoMode: Boolean = true,         // Battery Saver Enabled by Default
    val isDeviceMoving: Boolean = false     // Motion trigger state
)
