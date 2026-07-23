package com.architect.ojas.domain.model

/**
 * Represents the physical state of the environment as captured by the sensors.
 * This data drives the AGSL "Liquid Metal" shader.
 */
data class OjasState(
    val magneticFieldIntensity: Float = 0f, // Normalized 0.0 - 1.0 (Magnetometer)
    val airPressureDensity: Float = 0f,    // Normalized 0.0 - 1.0 (Barometer)
    val lightLumenLevel: Float = 0f,       // Normalized 0.0 - 1.0 (Light Sensor)
    val motionTurbulence: Float = 0f,      // Derived from Accelerometer/Gyro
    val isSystemStable: Boolean = true      // Calculated based on sensor variance
)
