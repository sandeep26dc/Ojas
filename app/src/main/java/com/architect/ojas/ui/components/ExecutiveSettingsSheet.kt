package com.architect.ojas.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.ojas.domain.model.OjasState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecutiveSettingsSheet(
    state: OjasState,
    onToggleFluidMode: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSensitivityChange: (Float) -> Unit,
    onToggleEcoMode: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var showDossier by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0A0C14),
        scrimColor = Color.Black.copy(alpha = 0.75f),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .background(Color(0xFF8A6513), RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CONTROL MATRIX",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700),
                        letterSpacing = 3.sp
                    )
                    Text(
                        text = "Hardware Telemetry & Neural Parameters",
                        fontSize = 11.sp,
                        color = Color(0xFF8E94A5)
                    )
                }

                // Executive Info Action Button
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF181B28))
                        .border(1.dp, Color(0xFFFFD700), CircleShape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showDossier = !showDossier
                        }
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Architect Info",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Expandable Architect Dossier & Info
            AnimatedVisibility(visible = showDossier) {
                ArchitectDossierCard()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Control Node 1: Fluid Rendering Core
            FuturisticNodeToggle(
                title = "AGSL RENDER MATRIX",
                subtitle = if (state.isFluidMode) "FERROFLUID CORE ACTIVE" else "LIQUID METAL CORE ACTIVE",
                isActive = state.isFluidMode,
                onToggle = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onToggleFluidMode()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Control Node 2: Adaptive Battery Throttling
            FuturisticNodeToggle(
                title = "ADAPTIVE POWER CORE",
                subtitle = if (state.isEcoMode) "LOW-POWER THROTTLING (10Hz)" else "HIGH-PERFORMANCE MATRIX (60Hz)",
                isActive = state.isEcoMode,
                activeColor = Color(0xFF00FFB2),
                onToggle = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onToggleEcoMode()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Control Node 3: Capacitive Audio Modulation Slider
            Text(
                text = "SYNTHETIC AUDIO HARMONICS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                letterSpacing = 1.5.sp
            )
            Slider(
                value = state.audioVolume,
                onValueChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onVolumeChange(it)
                },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFFD700),
                    activeTrackColor = Color(0xFF8A6513),
                    inactiveTrackColor = Color(0xFF1C2030)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FuturisticNodeToggle(
    title: String,
    subtitle: String,
    isActive: Boolean,
    activeColor: Color = Color(0xFFFFD700),
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF121522))
            .border(
                1.dp,
                if (isActive) activeColor.copy(alpha = 0.6f) else Color(0xFF222638),
                RoundedCornerShape(16.dp)
            )
            .clickable { onToggle() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = activeColor,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Custom Capacitive Glowing Node Indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isActive) activeColor else Color(0xFF222638))
                    .shadow(if (isActive) 12.dp else 0.dp, CircleShape, spotColor = activeColor)
            )
        }
    }
}

@Composable
private fun ArchitectDossierCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1B170B), Color(0xFF0F111A))
                )
            )
            .border(1.dp, Color(0xFF8A6513), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "EXECUTIVE SYSTEM DOSSIER",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFFD700),
            letterSpacing = 2.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Lead Architect: Sandeep Som",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Ojas (ओजस्) operates as an air-gapped sensory lens that translates environmental forces—magnetism, light, atmospheric pressure, acoustics, and gravity—into real-time AGSL fluid artwork and synthetic PCM sound waves.",
            fontSize = 11.sp,
            color = Color(0xFFB3B8C8),
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Divider(color = Color(0xFF33290A))

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "VERSION ARCHITECTURE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700),
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "• v2.0.0 (Current): Integrated multi-sensor array (Magnetometer, Barometer, Mic Blow), high-density gold aura launcher, adaptive battery throttling, and clean offline architecture.",
            fontSize = 10.sp,
            color = Color(0xFF8E94A5),
            lineHeight = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "• v1.0.0 (Legacy): Prototype motion tracker with standard default system icon.",
            fontSize = 10.sp,
            color = Color(0xFF5A6075),
            lineHeight = 14.sp
        )
    }
}
