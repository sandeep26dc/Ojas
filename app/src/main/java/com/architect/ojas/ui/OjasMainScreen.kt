package com.architect.ojas.ui

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.ojas.domain.model.OjasState
import com.architect.ojas.ui.components.TopographicGrid
import com.architect.ojas.ui.shader.LiquidMetalShader

@RequiresApi(Build.VERSION_33)
@Composable
fun OjasMainScreen(state: OjasState) {
    val shader = remember { RuntimeShader(LiquidMetalShader.CODE) }
    val infiniteTransition = rememberInfiniteTransition(label = "flux")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(100000, easing = LinearEasing), RepeatMode.Restart),
        label = "time"
    )

    var showInfo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Layer 1: The Mercury Physics Shader
        Canvas(modifier = Modifier.fillMaxSize()) {
            shader.setFloatUniform("uSize", size.width, size.height)
            shader.setFloatUniform("uTime", time)
            shader.setFloatUniform("uMagneticFlux", state.magneticFieldIntensity)
            shader.setFloatUniform("uPressure", state.airPressureDensity)
            shader.setFloatUniform("uLumen", state.lightLumenLevel)
            drawRect(brush = ShaderBrush(shader))
        }

        // Layer 2: The Architectural Grid
        TopographicGrid(pressure = state.airPressureDensity)

        // Layer 3: Subtle Branding/Info
        IconButton(
            onClick = { showInfo = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp).size(32.dp),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White.copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Info, contentDescription = "Info", modifier = Modifier.size(18.dp))
        }

        if (showInfo) {
            AlertDialog(
                onDismissRequest = { showInfo = false },
                containerColor = Color(0xFF0A0A0A),
                titleContentColor = Color.White,
                textContentColor = Color.LightGray,
                title = { Text("Ojas v1.0.0-Flux", fontSize = 18.sp) },
                text = {
                    Column {
                        Text("Lead Architect: Sandeep Som")
                        Text("Engine: Kotlin 2.0 / AGSL Mercury")
                        Text("Handle: sandeep26dc")
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Sensory Input: Active", color = Color(0xFF00FFCC), fontSize = 10.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showInfo = false }) {
                        Text("Close", color = Color.White)
                    }
                }
            )
        }
    }
}
