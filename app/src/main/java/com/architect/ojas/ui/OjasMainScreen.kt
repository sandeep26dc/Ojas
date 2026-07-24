package com.architect.ojas.ui

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.ojas.domain.model.OjasState
import com.architect.ojas.ui.components.ExecutiveDashboard
import com.architect.ojas.ui.components.FluidToggle
import com.architect.ojas.ui.components.ShadowGrid
import com.architect.ojas.ui.components.SmartPowerShaderCanvas
import com.architect.ojas.ui.shader.LiquidMetalShader

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OjasMainScreen(
    state: OjasState,
    onAudioToggle: (Boolean) -> Unit,
    onViscosityChange: (Float) -> Unit
) {
    val shader = remember { RuntimeShader(LiquidMetalShader.CODE) }
    var localViscosity by remember { mutableFloatStateOf(5.0f) }
    var zenModeActive by remember { mutableStateOf(false) }
    var showInfo by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "engine")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, 
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(200000, easing = LinearEasing)), 
        label = "time"
    )

    // Update AGSL Shader Uniforms continuously with sensor input
    SideEffect {
        shader.setFloatUniform("uTime", time)
        shader.setFloatUniform("uMagneticFlux", state.fluxIntensity)
        shader.setFloatUniform("uPressure", state.tiltZ)
        shader.setFloatUniform("uLumen", state.ambientLight)
        shader.setFloatUniform("uViscosity", localViscosity)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    localViscosity = (localViscosity + dragAmount / 50f).coerceIn(1.0f, 15.0f)
                    onViscosityChange(localViscosity)
                }
            }
    ) {
        // Smart Power Shader Canvas freezes GPU draw calls when the phone is static
        SmartPowerShaderCanvas(
            state = state,
            shaderBrush = ShaderBrush(shader)
        )

        ShadowGrid(pressure = state.tiltZ)
        ExecutiveDashboard(state = state)

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            FluidToggle(label = "ZEN_AUDIO", isActive = zenModeActive) {
                zenModeActive = it
                onAudioToggle(it)
            }
            IconButton(onClick = { showInfo = true }) {
                Icon(Icons.Default.Info, "Manifest", tint = Color.White.copy(alpha = 0.3f))
            }
        }

        if (showInfo) {
            OjasInfoDialog(onDismiss = { showInfo = false })
        }
    }
}

@Composable
fun OjasInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0A0C14),
        title = { 
            Text(
                "SYSTEM MANIFEST", 
                color = Color(0xFFFFD700), 
                letterSpacing = 2.sp,
                fontSize = 16.sp
            ) 
        },
        text = {
            Column {
                Text(
                    "ARCHITECT: Sandeep Som", 
                    color = Color.White, 
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "VERSION: 2.0.0 (Executive Overhaul)", 
                    color = Color(0xFFFFD700), 
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Ojas is a sensory matrix that translates environmental vectors—motion, gravity, light, acoustics, and air pressure—into liquid AGSL fluid art and motion-reactive PCM sound synthesis.",
                    color = Color(0xFF8E94A5),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        },
        confirmButton = { 
            TextButton(onClick = onDismiss) { 
                Text("DISMISS", color = Color(0xFFFFD700)) 
            } 
        }
    )
}
