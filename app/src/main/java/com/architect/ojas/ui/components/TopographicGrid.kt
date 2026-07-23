package com.architect.ojas.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopographicGrid(pressure: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 40.dp.toPx()
        val color = Color.White.copy(alpha = 0.05f + (pressure * 0.1f))
        
        // Vertical Lines
        for (x in 0..size.width.toInt() step step.toInt()) {
            drawLine(
                color = color,
                start = Offset(x.toFloat(), 0f),
                end = Offset(x.toFloat(), size.height),
                strokeWidth = 1f
            )
        }
        
        // Horizontal Lines
        for (y in 0..size.height.toInt() step step.toInt()) {
            drawLine(
                color = color,
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
            )
        }
    }
}
