package com.architect.ojas.ui.components

import android.graphics.RuntimeShader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import com.architect.ojas.domain.model.OjasState

@Composable
fun PassiveFluxOrb(
    state: OjasState,
    shader: RuntimeShader
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        shader.setFloatUniform("uMagneticFlux", state.fluxIntensity)
        shader.setFloatUniform("uGForce", state.tiltX, state.tiltY, state.tiltZ)
        drawRect(brush = ShaderBrush(shader))
    }
}
