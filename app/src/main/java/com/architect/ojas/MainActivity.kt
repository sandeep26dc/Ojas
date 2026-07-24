package com.architect.ojas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.core.content.ContextCompat
import com.architect.ojas.ui.OjasMainScreen
import com.architect.ojas.ui.OjasViewModel
import com.architect.ojas.ui.components.DivineStartupSplash
import com.architect.ojas.ui.components.ExecutiveSettingsSheet

class MainActivity : ComponentActivity() {
    private val viewModel: OjasViewModel by viewModels()

    private val requestAudioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        setContent {
            var isSplashVisible by rememberSaveable { mutableStateOf(true) }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF090A0F)
            ) {
                Crossfade(
                    targetState = isSplashVisible,
                    animationSpec = tween(durationMillis = 800),
                    label = "SplashTransition"
                ) { showSplash ->
                    if (showSplash) {
                        DivineStartupSplash(onSplashComplete = { isSplashVisible = false })
                    } else {
                        ExecutiveAppContent(viewModel = viewModel)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startSensors()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopSensors()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ExecutiveAppContent(viewModel: OjasViewModel) {
    val state by viewModel.uiState.collectAsState()
    var showSettingsSheet by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A0F))
    ) {
        OjasMainScreen(
            state = state,
            onAudioToggle = { active -> viewModel.setMasterVolume(if (active) 0.8f else 0.0f) },
            onViscosityChange = { v -> viewModel.setSensorSensitivity(v) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFFD700), Color(0xFF6B5200))
                            )
                        )
                        .padding(1.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFF0F111A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ओ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "OJAS",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "EXECUTIVE VITALITY",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFD700),
                        letterSpacing = 1.2.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF131520).copy(alpha = 0.85f))
                    .border(1.dp, Color(0xFF4A3B00), RoundedCornerShape(14.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showSettingsSheet = true
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Control Matrix",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "MATRIX",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        if (showSettingsSheet) {
            ExecutiveSettingsSheet(
                state = state,
                onToggleFluidMode = { viewModel.toggleFluidMode() },
                onVolumeChange = { viewModel.setMasterVolume(it) },
                onSensitivityChange = { viewModel.setSensorSensitivity(it) },
                onToggleEcoMode = { viewModel.toggleEcoPowerMode() },
                onDismiss = { showSettingsSheet = false }
            )
        }
    }
}
