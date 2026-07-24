package com.architect.ojas.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DivineStartupSplash(
    onSplashComplete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var startAnimation by remember { mutableStateOf(false) }

    val auraScale by animateFloatAsState(
        targetValue = if (startAnimation) 1.2f else 0.2f,
        animationSpec = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
        label = "auraScale"
    )

    val auraAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "auraAlpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        delay(1000)
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        delay(1800)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A0F)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(320.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val baseRadius = (size.minDimension / 3) * auraScale

            // Multi-Layer Divine Aura Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF8A6513).copy(alpha = 0.6f * auraAlpha),
                        Color(0xFF131520).copy(alpha = 0.1f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 1.8f
                ),
                center = center,
                radius = baseRadius * 1.8f
            )

            // Sacred Geometrical Lotus Rays
            rotate(rotationAngle, pivot = center) {
                val petals = 12
                for (i in 0 until petals) {
                    val angle = (i * 360f / petals) * (Math.PI / 180f)
                    val endX = center.x + cos(angle).toFloat() * baseRadius
                    val endY = center.y + sin(angle).toFloat() * baseRadius
                    
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFFD700), Color(0x00FFD700))
                        ),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Inner Sacred Geometry Rings
                drawCircle(
                    color = Color(0xFFFFD700).copy(alpha = 0.8f * auraAlpha),
                    radius = baseRadius * 0.6f,
                    style = Stroke(width = 1.5.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFFFFF8DC).copy(alpha = 0.5f * auraAlpha),
                    radius = baseRadius * 0.35f,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp)
                .alpha(auraAlpha)
        ) {
            Text(
                text = "ओजस्",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "O J A S",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                letterSpacing = 8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "THE DIVINE ESSENCE OF VITALITY",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFB39700),
                letterSpacing = 2.sp
            )
        }
    }
}
