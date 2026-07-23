package com.architect.ojas.ui.shader

import org.intellij.lang.annotations.Language

object FerrofluidShader {
    @Language("AGSL")
    val CODE = """
        uniform float2 uSize;
        uniform float uTime;
        uniform float uMagneticFlux;
        uniform float3 uGravity; // Accelerometer data

        float snoise(float3 v) {
            // Simplified noise for Ferrofluid "spikes"
            return fract(sin(dot(v, float3(12.9898, 78.233, 45.164))) * 43758.5453);
        }

        half4 main(float2 fragCoord) {
            float2 uv = fragCoord / uSize.xy;
            float2 p = -1.0 + 2.0 * uv;
            p.x *= uSize.x / uSize.y;

            // Gravity influence: The liquid "settles" toward the bottom of the phone
            float2 gravityOffset = uGravity.xy * 0.1;
            p -= gravityOffset;

            // The "Core" Ferrofluid Blob
            float dist = length(p);
            
            // Magnetic "Spiking" effect
            // As uMagneticFlux increases, the edges become jagged
            float spikes = sin(atan2(p.y, p.x) * 20.0) * uMagneticFlux * 0.1;
            float blob = smoothstep(0.4 + spikes, 0.38 + spikes, dist);

            // Lighting: Dark Chrome / Mercury
            float3 color = lerp(float3(0.01, 0.01, 0.02), float3(0.6, 0.6, 0.7), blob);
            
            // Specular highlight on the blob
            float spec = pow(max(0.0, 1.0 - length(p - float2(0.1, 0.1))), 10.0) * blob;
            color += spec * 0.4;

            return half4(color, blob); // Alpha is driven by the blob shape
        }
    """.trimIndent()
}
