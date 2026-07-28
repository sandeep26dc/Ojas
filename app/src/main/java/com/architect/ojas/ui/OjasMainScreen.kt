package com.architect.ojas.ui

import android.graphics.RuntimeShader
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.ojas.domain.model.OjasState
import com.architect.ojas.ui.components.ExecutiveDashboard
import com.architect.ojas.ui.components.FluidToggle
import com.architect.ojas.ui.components.GranularOrbView
import com.architect.ojas.ui.components.SmartPowerShaderCanvas
import com.architect.ojas.ui.shader.LiquidMetalShader
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OjasMainScreen(
    state: OjasState,
    onAudioToggle: (Boolean) -> Unit,
    onViscosityChange: (Float) -> Unit
) {
    // Safe initialization with try-catch to prevent Mali GPU driver crashes on startup
    val shader = remember {
        try {
            RuntimeShader(LiquidMetalShader.CODE)
        } catch (e: Exception) {
            Log.e("OjasShader", "Failed to compile AGSL shader, falling back", e)
            null
        }
    }

    var localViscosity by remember { mutableFloatStateOf(5.0f) }
    var zenModeActive by remember { mutableStateOf(false) }
    var showInfo by remember { mutableStateOf(false) }
    
    // Safety buffer for GPU context handoff after splash animation
    var isCanvasReady by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(150) // Allow splash screen transition to clear completely
        isCanvasReady = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "engine")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, 
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(200000, easing = LinearEasing)), 
        label = "time"
    )

    // Update AGSL Shader Uniforms safely if shader compiled successfully
    SideEffect {
        shader?.let {
            try {
                it.setFloatUniform("uTime", time)
                it.setFloatUniform("uMagneticFlux", state.fluxIntensity)
                it.setFloatUniform("uPressure", state.tiltZ)
                it.setFloatUniform("uLumen", state.ambientLight)
                it.setFloatUniform("uViscosity", localViscosity)
            } catch (e: Exception) {
                Log.e("OjasShader", "Error updating uniforms", e)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030712)) // Deep cosmic obsidian background for high contrast
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    localViscosity = (localViscosity + dragAmount / 50f).coerceIn(1.0f, 15.0f)
                    onViscosityChange(localViscosity)
                }
            }
    ) {
        // Only trigger canvas and shader rendering after the transition delay clears
        if (isCanvasReady) {
            if (shader != null) {
                SmartPowerShaderCanvas(
                    state = state,
                    shaderBrush = ShaderBrush(shader)
                )
            } else {
                // Fallback background canvas if shader compilation failed completely
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF030712)))
            }
        }

        // --- SCI-FI GRANULAR ORB EXPERIENCE (FRIDAY / JARVIS ENGINE) ---
        // Fluctuates and disperses based on active magnetic flux & tilt telemetry
        GranularOrbView(
            modifier = Modifier.fillMaxSize(),
            activityLevel = state.fluxIntensity.coerceIn(0.1f, 1.5f)
        )

        // High-Contrast Executive Dashboard & Metrics Overlay
        ExecutiveDashboard(state = state)

        // Bottom Controls Toolbar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FluidToggle(label = "ZEN_AUDIO", isActive = zenModeActive) {
                zenModeActive = it
                onAudioToggle(it)
            }
            IconButton(onClick = { showInfo = true }) {
                Icon(Icons.Default.Info, "Manifest", tint = Color(0xFFFFD700).copy(alpha = 0.8f))
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
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            ) 
        },
        text = {
            Column {
                Text(
                    "ARCHITECT: Sandeep Som", 
                    color = Color.White, 
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "VERSION: 2.0.0 (Executive Overhaul)", 
                    color = Color(0xFFFFD700), 
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Ojas is a sensory matrix that translates environmental vectors—motion, gravity, light, acoustics, and air pressure—into liquid AGSL fluid art and motion-reactive PCM sound synthesis.",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        confirmButton = { 
            TextButton(onClick = onDismiss) { 
                Text("DISMISS", color = Color(0xFFFFD700), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) 
            } 
        }
    )
}
