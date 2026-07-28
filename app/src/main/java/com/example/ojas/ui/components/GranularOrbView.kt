package com.example.ojas.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GranularOrbView(
    modifier: Modifier = Modifier,
    activityLevel: Float = 0.5f // Fluctuates based on telemetry data
) {
    val infiniteTransition = rememberInfiniteTransition(label = "OrbTransition")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbRotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f + (activityLevel * 0.1f),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = size.minDimension * 0.28f * pulseScale

        // Outer holographic rings
        drawCircle(
            color = Color(0xFF38BDF8).copy(alpha = 0.2f),
            radius = baseRadius * 1.4f,
            center = center,
            style = Stroke(width = 2f)
        )
        drawCircle(
            color = Color(0xFF818CF8).copy(alpha = 0.15f),
            radius = baseRadius * 1.7f,
            center = center,
            style = Stroke(width = 1.5f)
        )

        // Granular Particle Ring (The Jarvis/FRIDAY Orb Grains)
        val particleCount = 72
        for (i in 0 until particleCount) {
            val angleDeg = (i * (360f / particleCount)) + rotationAngle
            val angleRad = Math.toRadians(angleDeg.toDouble())

            // Disperse/fluctuate grains based on activity level
            const val jitter = 8f
            val dynamicRadius = baseRadius + (sin((i + rotationAngle).toDouble()) * (6f + (activityLevel * 15f))).toFloat()

            val x = center.x + (dynamicRadius * cos(angleRad)).toFloat()
            val y = center.y + (dynamicRadius * sin(angleRad)).toFloat()

            drawCircle(
                color = if (i % 2 == 0) Color(0xFF38BDF8) else Color(0xFFC084FC),
                radius = 3.5f + (activityLevel * 2f),
                center = Offset(x, y)
            )
        }

        // Core Glowing Orb Nucleus
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF38BDF8).copy(alpha = 0.8f),
                    Color(0xFF6366F1).copy(alpha = 0.4f),
                    Color.Transparent
                ),
                center = center,
                radius = baseRadius
            ),
            radius = baseRadius,
            center = center
        )
    }
}
