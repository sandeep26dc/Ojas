package com.architect.ojas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.architect.ojas.data.sensor.SensorProvider
import com.architect.ojas.ui.OjasMainScreen
import com.architect.ojas.ui.OjasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Manual DI for simplicity in this Architect Build
        val sensorProvider = SensorProvider(applicationContext)
        val viewModel = OjasViewModel(sensorProvider)

        setContent {
            val state by viewModel.uiState.collectAsState()
            OjasMainScreen(state = state)
        }
    }
}
