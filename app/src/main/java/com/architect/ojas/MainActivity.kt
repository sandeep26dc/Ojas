package com.architect.ojas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.architect.ojas.data.sensor.SensorProvider
import com.architect.ojas.ui.OjasMainScreen
import com.architect.ojas.ui.OjasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize the Sensor Provider
        val sensorProvider = SensorProvider(applicationContext)

        // 2. Create the Factory to pass 'Application' and 'SensorProvider' to the ViewModel
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OjasViewModel(application, sensorProvider) as T
            }
        }

        // 3. Obtain the ViewModel
        val viewModel = ViewModelProvider(this, viewModelFactory)[OjasViewModel::class.java]

        setContent {
            val state by viewModel.uiState.collectAsState()
            
            // Pass the state and the toggle functions to the UI
            OjasMainScreen(
                state = state,
                onAudioToggle = { enabled -> viewModel.toggleZenMode(enabled) },
                onViscosityChange = { value -> viewModel.updateViscosity(value) }
            )
        }
    }
}
