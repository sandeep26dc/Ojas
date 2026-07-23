package com.architect.ojas.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.architect.ojas.audio.ZenEngine
import com.architect.ojas.data.sensor.SensorProvider
import com.architect.ojas.domain.model.OjasState
import com.architect.ojas.haptics.HapticEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OjasViewModel(
    application: Application,
    private val sensorProvider: SensorProvider
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(OjasState())
    val uiState: StateFlow<OjasState> = _uiState.asStateFlow()

    // Sensory Engines
    private val hapticEngine = HapticEngine(application)
    private val zenEngine = ZenEngine()

    // Local control states
    private var isZenEnabled = false
    private var currentViscosity = 5.0f

    init {
        startObservingSensors()
    }

    private fun startObservingSensors() {
        viewModelScope.launch {
            sensorProvider.getSensorFlux().collect { newState ->
                _uiState.value = newState
                
                // Trigger Trinity Sync: Visuals (via state), Audio, and Haptics
                performSensoryFeedback(newState)
            }
        }
    }

    private fun performSensoryFeedback(state: OjasState) {
        // 1. Audio Update
        if (isZenEnabled) {
            zenEngine.updateAudio(state.magneticFieldIntensity, state.airPressureDensity)
        }

        // 2. Haptic Update (Triggered when magnetic flux crosses a threshold)
        if (state.magneticFieldIntensity > 0.2f) {
            hapticEngine.pulse(state.magneticFieldIntensity, currentViscosity)
        }
    }

    fun toggleZenMode(enabled: Boolean) {
        isZenEnabled = enabled
        if (enabled) zenEngine.start() else zenEngine.stop()
    }

    fun updateViscosity(value: Float) {
        currentViscosity = value
    }

    override fun onCleared() {
        super.onCleared()
        zenEngine.stop() // Prevent memory/audio leaks
    }
}
