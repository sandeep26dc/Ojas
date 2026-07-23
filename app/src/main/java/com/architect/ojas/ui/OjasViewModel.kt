package com.architect.ojas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architect.ojas.data.sensor.SensorProvider
import com.architect.ojas.domain.model.OjasState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OjasViewModel(private val sensorProvider: SensorProvider) : ViewModel() {

    private val _uiState = MutableStateFlow(OjasState())
    val uiState: StateFlow<OjasState> = _uiState.asStateFlow()

    init {
        startObservingSensors()
    }

    private fun startObservingSensors() {
        viewModelScope.launch {
            sensorProvider.getSensorFlux().collect { newState ->
                _uiState.value = newState
            }
        }
    }
}
