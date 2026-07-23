package com.architect.ojas.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ShadowGrid(pressure: Float) {
    // The grid 'glows' and becomes more opaque as pressure rises
    val gridAlpha = (0.05f + (pressure * 0.4f)).coerceIn(0.05f, 0.5f)
    val glowAlpha = (pressure * 0.2f).coerceIn(0f, 0.2f)
    val gridColor = Color(0xFF00FFFF).copy(alpha = gridAlpha) // Cyan Glow
    val shadowColor = Color.Black.copy(alpha = 0.8f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 50.dp.toPx()
        
        // Horizontal Lines
        for (y in 0..size.height.toInt() step step.toInt()) {
            // Draw a black "shadow" line behind for depth
            drawLine(
                color = shadowColor,
                start = androidx.compose.ui.geometry.Offset(0f, y.toFloat() + 2f),
                end = androidx.compose.ui.geometry.Offset(size.width, y.toFloat() + 2f),
                strokeWidth = 1.dp.toPx()
            )
            // Draw the "Architect" Cyan line
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(0f, y.toFloat()),
                end = androidx.compose.ui.geometry.Offset(size.width, y.toFloat()),
                strokeWidth = 0.5.dp.toPx()
            )
        }

        // Vertical Lines
        for (x in 0..size.width.toInt() step step.toInt()) {
            drawLine(
                color = shadowColor,
                start = androidx.compose.ui.geometry.Offset(x.toFloat() + 2f, 0f),
                end = androidx.compose.ui.geometry.Offset(x.toFloat() + 2f, size.height),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(x.toFloat(), 0f),
                end = androidx.compose.ui.geometry.Offset(x.toFloat(), size.height),
                strokeWidth = 0.5.dp.toPx()
            )
        }
    }
}
