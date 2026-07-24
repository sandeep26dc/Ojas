package com.architect.ojas.domain.model

data class OjasState(
    // Accelerometer & Gyro Matrix
    val tiltX: Float = 0f,
    val tiltY: Float = 0f,
    val tiltZ: Float = 0f,
    val gyroX: Float = 0f,
    val gyroY: Float = 0f,
    val gyroZ: Float = 0f,

    // Environment & Atmospheric Sensors
    val ambientLight: Float = 0f,           // Lux
    val magneticField: Float = 0f,          // Micro-Tesla (uT)
    val atmosphericPressure: Float = 0f,    // hPa / mbar
    val soundAcousticDb: Float = 0f,        // Microphone level (Blow/Acoustic pressure)

    // Calculated Executive Metrics
    val fluxIntensity: Float = 0f,
    val isFluidMode: Boolean = true,
    val audioVolume: Float = 0.8f
)
