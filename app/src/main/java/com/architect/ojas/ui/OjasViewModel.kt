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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OjasViewModel(application: Application) : AndroidViewModel(application) {

    private val sensorProvider = SensorProvider(application)
    private val zenEngine = ZenEngine()
    private val hapticEngine = HapticEngine(application)

    val uiState: StateFlow<OjasState> = sensorProvider.state

    init {
        viewModelScope.launch {
            uiState.collect { state ->
                // Dynamically modulate synthesized audio based on tilt & acoustics
                zenEngine.updateFrequencyAndVolume(
                    tiltX = state.tiltX,
                    tiltY = state.tiltY,
                    acousticDb = state.soundAcousticDb,
                    masterVolume = state.audioVolume
                )

                // Trigger subtle haptic pulses on flux peaks
                if (state.fluxIntensity > 14f) {
                    hapticEngine.triggerFluxPeakHaptic()
                }
            }
        }
    }

    fun startSensors() {
        sensorProvider.startListening()
        zenEngine.start()
    }

    fun stopSensors() {
        sensorProvider.stopListening()
        zenEngine.stop()
    }

    fun toggleFluidMode() {
        _stateUpdate { it.copy(isFluidMode = !it.isFluidMode) }
    }

    fun setMasterVolume(volume: Float) {
        _stateUpdate { it.copy(audioVolume = volume) }
    }

    fun setSensorSensitivity(sensitivity: Float) {
        _stateUpdate { it.copy(sensorSensitivity = sensitivity) }
    }

    fun toggleEcoPowerMode() {
        val newMode = !uiState.value.isEcoMode
        _stateUpdate { it.copy(isEcoMode = newMode) }
        sensorProvider.setEcoMode(newMode)
    }

    private fun _stateUpdate(transform: (OjasState) -> OjasState) {
        sensorProvider.updateState(transform)
    }
}
