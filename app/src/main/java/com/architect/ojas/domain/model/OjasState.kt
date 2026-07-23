package com.architect.ojas.domain.model

data class OjasState(
    val magneticFieldIntensity: Float = 0f,
    val airPressureDensity: Float = 0f,
    val lightLumenLevel: Float = 0f,
    val motionTurbulence: Float = 0f,
    val gForceVector: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
    val isLockedMode: Boolean = false // Track if we are in "Passive/AOD" mode
)
