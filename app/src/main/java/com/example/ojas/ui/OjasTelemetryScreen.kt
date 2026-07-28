package com.example.ojas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ojas.ui.components.GranularOrbView

@Composable
fun OjasTelemetryScreen() {
    var telemetryActivity by remember { mutableStateOf(0.6f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF030712), Color(0xFF0F172A), Color(0xFF030712))
                )
            )
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Central Granular Orb Animation reacting to telemetry
        GranularOrbView(
            modifier = Modifier.fillMaxSize(),
            activityLevel = telemetryActivity
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top HUD Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E293B).copy(alpha = 0.8f)
                ) {
                    Text(
                        text = "ॐ OJAS TELEMETRY",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        color = Color(0xFF38BDF8),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E293B).copy(alpha = 0.8f)
                ) {
                    Text(
                        text = "MATRIX ACTIVE",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        color = Color(0xFF34D399),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            // Live Telemetry Data Cards Overlay
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TelemetryCard(
                    title = "MAG FLUX",
                    value = "98.5%",
                    modifier = Modifier.weight(1f)
                )
                TelemetryCard(
                    title = "ATM PRESS",
                    value = "600 hPa",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TelemetryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0F172A).copy(alpha = 0.85f),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spaced espaBy = 4.dp
        ) {
            Text(
                text = title,
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
