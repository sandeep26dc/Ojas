package com.architect.ojas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.ojas.domain.model.OjasState

@Composable
fun ExecutiveDashboard(state: OjasState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 40.dp) // Space for status bar
    ) {
        // Top Header
        Text(
            text = "SENSORY TELEMETRY",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 12.sp,
            letterSpacing = 4.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Grid Layout for Sensors
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SensorItem(label = "MAG_FLUX", value = "${(state.magneticFieldIntensity * 100).toInt()}%")
            SensorItem(label = "ATM_PRES", value = "${(state.airPressureDensity * 1000).toInt()} hPa")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SensorItem(label = "LUMEN", value = "${(state.lightLumenLevel * 100).toInt()} lx")
            SensorItem(label = "STABILITY", value = if (state.isSystemStable) "OPTIMAL" else "CHAOS")
        }
    }
}

@Composable
fun SensorItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.3f),
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
