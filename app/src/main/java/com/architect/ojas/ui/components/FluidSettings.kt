package com.architect.ojas.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FluidToggle(label: String, isActive: Boolean, onToggle: (Boolean) -> Unit) {
    val heightScale by animateFloatAsState(if (isActive) 1f else 0.1f, label = "fill")
    
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .pointerInput(Unit) {
                    detectTapGestures { onToggle(!isActive) }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            // The "Mercury" level inside the toggle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(heightScale)
                    .background(if (isActive) Color.Cyan.copy(alpha = 0.6f) else Color.Gray.copy(alpha = 0.2f))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}
