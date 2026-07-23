package com.architect.ojas.ui.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import com.architect.ojas.domain.model.OjasState
import com.architect.ojas.ui.shader.FerrofluidShader

@RequiresApi(Build.VERSION_33)
@Composable
fun PassiveFluxOrb(state: OjasState, time: Float) {
    val shader = RuntimeShader(FerrofluidShader.CODE)
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        shader.setFloatUniform("uSize", size.width, size.height)
        shader.setFloatUniform("uTime", time)
        shader.setFloatUniform("uMagneticFlux", state.magneticFieldIntensity)
        shader.setFloatUniform("uGravity", 
            state.gForceVector.first, 
            state.gForceVector.second, 
            state.gForceVector.third
        )
        
        drawRect(brush = ShaderBrush(shader))
    }
}
