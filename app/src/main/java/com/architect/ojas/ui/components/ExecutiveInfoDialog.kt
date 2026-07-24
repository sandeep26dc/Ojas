package com.architect.ojas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ExecutiveInfoDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF0F111A),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .shadow(24.dp, RoundedCornerShape(28.dp))
                .border(
                    1.dp,
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFFD700), Color(0xFF4A3B00))
                    ),
                    RoundedCornerShape(28.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "OJAS EXECUTIVE",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700),
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Divine Vitality Matrix",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                    Text(
                        text = "v2.0.0",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFFFD700),
                        modifier = Modifier
                            .background(Color(0xFF261E05), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Philosophy Section
                Text(
                    text = "THE CONCEPT OF OJAS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ojas (ओजस्) represents the refined essence of physical vitality, mental clarity, and spiritual radiance. This $10,000 executive suite translates real-time sensor dynamics into dynamic AGSL fluid simulations, generative soundscapes, and tactile haptic feedback.",
                    fontSize = 12.sp,
                    color = Color(0xFFC5C7D0),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Version History
                Text(
                    text = "RELEASE HISTORY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.height(10.dp))

                VersionItem(
                    version = "v2.0.0 — Executive Divine Overhaul",
                    details = listOf(
                        "Divine Aura startup sequence with sacred lotus geometry",
                        "High-density executive launcher icon",
                        "AGSL Ferrofluid & Liquid Metal dual shader engine",
                        "Real-time sensor telemetry dashboard with gold accents",
                        "Precision spring physical micro-haptics"
                    ),
                    isCurrent = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                VersionItem(
                    version = "v1.0.0 — Base Prototype",
                    details = listOf(
                        "Initial accelerometer sensor tracking",
                        "Basic canvas rendering and simple sound tone"
                    ),
                    isCurrent = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "DISMISS",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun VersionItem(
    version: String,
    details: List<String>,
    isCurrent: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isCurrent) Color(0xFF1D1808) else Color(0xFF141622),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (isCurrent) Color(0xFF8A6513) else Color(0xFF232738),
                RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = version,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isCurrent) Color(0xFFFFD700) else Color.White
        )
        Spacer(modifier = Modifier.height(6.dp))
        details.forEach { detail ->
            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                Text(
                    text = "• ",
                    color = Color(0xFFFFD700),
                    fontSize = 12.sp
                )
                Text(
                    text = detail,
                    fontSize = 11.sp,
                    color = Color(0xFFA0A5B5)
                )
            }
        }
    }
}
