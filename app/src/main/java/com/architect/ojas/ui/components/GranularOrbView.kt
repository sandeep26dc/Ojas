package com.architect.ojas.ui.components

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
    activityLevel: Float = 0.5f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "OrbTransition")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbRotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f + (activityLevel * 0.12f),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.RepeatMode.Reverse
        ),
        label = "OrbPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = size.minDimension * 0.32f * pulseScale

        // Holographic Outer Targeting Rings
        drawCircle(
            color = Color(0xFFFFD700).copy(alpha = 0.15f),
            radius = baseRadius * 1.5f,
            center = center,
            style = Stroke(width = 1.5f)
        )
        drawCircle(
            color = Color(0xFF38BDF8).copy(alpha = 0.12f),
            radius = baseRadius * 1.8f,
            center = center,
            style = Stroke(width = 1f)
        )

        // Granular Particle Matrix Ring (Jarvis / FRIDAY Core Effect)
        val particleCount = 80
        for (i in 0 until particleCount) {
            val angleDeg = (i * (360f / particleCount)) + rotationAngle
            val angleRad = Math.toRadians(angleDeg.toDouble())

            val dynamicOffset = (sin((i * 5 + rotationAngle).toDouble()) * (8f + (activityLevel * 18f))).toFloat()
            val currentRadius = baseRadius + dynamicOffset

            val x = center.x + (currentRadius * cos(angleRad)).toFloat()
            val y = center.y + (currentRadius * sin(angleRad)).toFloat()

            drawCircle(
                color = if (i % 3 == 0) Color(0xFFFFD700) else Color(0xFF38BDF8),
                radius = 3f + (activityLevel * 2.5f),
                center = Offset(x, y)
            )
        }

        // Deep Core Plasma Nucleus
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD700).copy(alpha = 0.6f),
                    Color(0xFF38BDF8).copy(alpha = 0.3f),
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
