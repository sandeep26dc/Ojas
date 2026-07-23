package com.architect.ojas.ui

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import com.architect.ojas.ui.shader.LiquidMetalShader

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OjasMainScreen(
    state: OjasState,
    onAudioToggle: (Boolean) -> Unit,
    onViscosityChange: (Float) -> Unit
) {
    val shader = remember { RuntimeShader(LiquidMetalShader.CODE) }
    var localViscosity by remember { mutableStateOf(5.0f) }
    var zenModeActive by remember { mutableStateOf(false) }
    var showInfo by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "engine")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(200000, easing = LinearEasing)), label = "time"
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                localViscosity = (localViscosity + dragAmount / 50f).coerceIn(1.0f, 15.0f)
                onViscosityChange(localViscosity)
            }
        }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            shader.setFloatUniform("uSize", size.width, size.height)
            shader.setFloatUniform("uTime", time)
            shader.setFloatUniform("uMagneticFlux", state.magneticFieldIntensity)
            shader.setFloatUniform("uPressure", state.airPressureDensity)
            shader.setFloatUniform("uLumen", state.lightLumenLevel)
            shader.setFloatUniform("uViscosity", localViscosity)
            drawRect(brush = ShaderBrush(shader))
        }

        ShadowGrid(pressure = state.airPressureDensity)
        ExecutiveDashboard(state = state)

        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp),
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
        containerColor = Color(0xFF050505),
        title = { Text("SYSTEM MANIFEST", color = Color.White) },
        text = {
            Column {
                Text("ARCHITECT: Sandeep Som", color = Color.Cyan, fontSize = 14.sp)
                Text("VERSION: 1.0.0 (Ojas Mercury)", color = Color.Gray, fontSize = 12.sp)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("CLOSE", color = Color.White) } }
    )
}
