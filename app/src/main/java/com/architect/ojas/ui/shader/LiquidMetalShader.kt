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

            // Physical constant for Mercury: High Surface Tension
            float surfaceTension = 1.5 + (uPressure * 2.0);
            float3 movement = float3(p * surfaceTension, uTime * 0.4);
            
            // Flux Distortion
            float fluxBase = noise(movement + (uMagneticFlux * 3.0));
            
            // Calculate Normals (Pseudo-3D)
            float val = noise(movement);
            float valX = noise(movement + float3(0.01, 0.0, 0.0));
            float valY = noise(movement + float3(0.0, 0.01, 0.0));
            float3 normal = normalize(float3(valX - val, valY - val, 0.05));

            // Specular Lighting (Mercury Highlights)
            float3 lightDir = normalize(float3(1.0, 1.0, 1.0));
            float spec = pow(max(dot(normal, lightDir), 0.0), 32.0);
            
            // Spectral Iridescence (Only when Magnetic Flux is high)
            float3 iridescence = float3(
                sin(uMagneticFlux * 5.0 + 0.0),
                sin(uMagneticFlux * 5.0 + 2.0),
                sin(uMagneticFlux * 5.0 + 4.0)
            ) * 0.2 * uMagneticFlux;

            // Combine for Mercury Finish
            float3 baseChrome = float3(0.05, 0.05, 0.07); // Dark Base
            float3 mercury = lerp(baseChrome, float3(0.9, 0.9, 1.0), spec);
            mercury += iridescence; // Add the Flux charge
            
            // Apply Lumen for environmental dimming
            mercury *= (0.5 + uLumen * 0.5);

            return half4(mercury, 1.0);
        }
    """.trimIndent()
}
