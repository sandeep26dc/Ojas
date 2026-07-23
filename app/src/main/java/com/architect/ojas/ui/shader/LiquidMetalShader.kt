package com.architect.ojas.ui.shader

import org.intellij.lang.annotations.Language

object LiquidMetalShader {
    @Language("AGSL")
    val CODE = """
        uniform float2 uSize;
        uniform float uTime;
        uniform float uMagneticFlux;
        uniform float uPressure;
        uniform float uLumen;

        // Simple noise function for fluid turbulence
        float hash(float n) { return fract(sin(n) * 43758.5453123); }
        float noise(float3 x) {
            float3 p = floor(x);
            float3 f = fract(x);
            f = f * f * (3.0 - 2.0 * f);
            float n = p.x + p.y * 57.0 + 113.0 * p.z;
            return lerp(lerp(lerp(hash(n + 0.0), hash(n + 1.0), f.x),
                        lerp(hash(n + 57.0), hash(n + 58.0), f.x), f.y),
                   lerp(lerp(hash(n + 113.0), hash(n + 114.0), f.x),
                        lerp(hash(n + 170.0), hash(n + 171.0), f.x), f.y), f.z);
        }

        half4 main(float2 fragCoord) {
            float2 uv = fragCoord / uSize.xy;
            float2 p = -1.0 + 2.0 * uv;
            p.x *= uSize.x / uSize.y;

            // Use Magnetic Flux to distort the space
            float distortion = uMagneticFlux * 2.0;
            float3 movement = float3(p * (2.0 + uPressure), uTime * 0.5);
            float n = noise(movement + distortion);

            // Create "Liquid Metal" specular highlights
            float spec = pow(n, 10.0 + (1.0 - uLumen) * 20.0);
            
            // Base color: Deep Obsidian to Chrome
            float3 color = lerp(float3(0.02, 0.02, 0.05), float3(0.8, 0.8, 0.9), spec);
            
            // Add a subtle "Ojas" glow based on light sensor
            color += float3(0.1, 0.1, 0.15) * uLumen * n;

            return half4(color, 1.0);
        }
    """.trimIndent()
}
