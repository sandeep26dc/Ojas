package com.architect.ojas.ui

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.ojas.domain.model.OjasState
import com.architect.ojas.ui.components.ExecutiveDashboard
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

        // Layer 2: Architectural Grid
        TopographicGrid(pressure = state.airPressureDensity)

        // Layer 3: Executive Dashboard readouts
        ExecutiveDashboard(state = state)

        // Layer 4: Subtle Branding/Info Trigger
        IconButton(
            onClick = { showInfo = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp).size(32.dp),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White.copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Info, contentDescription = "Info", modifier = Modifier.size(18.dp))
        }

        // --- THE "i" SECTION ---
        if (showInfo) {
            AlertDialog(
                onDismissRequest = { showInfo = false },
                containerColor = Color(0xFF0A0A0A),
                titleContentColor = Color.White,
                textContentColor = Color.LightGray,
                title = { 
                    Text("OJAS: SENSORY HUD", fontWeight = FontWeight.Bold, letterSpacing = 2.sp) 
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text("ARCHITECT", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                        Text("Sandeep Som (sandeep26dc)", color = Color.White, fontSize = 14.sp)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("VERSION", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                        Text("1.0.0 — Mercury Build", color = Color.White, fontSize = 14.sp)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("SYSTEM REQUIREMENTS", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                        Text("• Android 13 (API 33) or higher\n" +
                             "• Hardware Magnetometer (EMF)\n" +
                             "• Hardware Barometer (Pressure)\n" +
                             "• Ambient Light Sensor\n" +
                             "• High-Performance GPU (AGSL Support)", color = Color.LightGray, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("WHAT IS OJAS?", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                        Text("Ojas is a 'Digital Twin' of your physical environment. It translates invisible forces—magnetic fields, atmospheric pressure, and light waves—into a living, generative material flux.", color = Color.LightGray, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("HOW IT HELPS", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                        Text("• EMF Awareness: Detect interference from electronics.\n" +
                             "• Weather Vigilance: Monitor pressure drops for incoming storms.\n" +
                             "• Stress Reduction: A dynamic, focus-oriented ambient HUD.\n" +
                             "• Zero Privacy Risk: 100% offline. No data leaves the device.", color = Color.LightGray, fontSize = 12.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showInfo = false }) {
                        Text("DISMISS", color = Color.Cyan)
                    }
                }
            )
        }
    }
}
