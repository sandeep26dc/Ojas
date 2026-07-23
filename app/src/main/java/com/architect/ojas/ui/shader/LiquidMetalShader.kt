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
        uniform float uViscosity; 

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

            // --- STEP 26: MASS INERTIA LOGIC ---
            // Higher viscosity makes the "Time" move slower, simulating weight/lag
            float massLag = lerp(0.9, 0.1, uViscosity / 15.0); 
            float3 movement = float3(p * uViscosity, uTime * (0.4 * massLag));
            
            float distortion = noise(movement + (uMagneticFlux * 2.5));
            
            // Specular Reflection (Mercury Surface)
            float val = noise(movement);
            float valX = noise(movement + float3(0.005, 0.0, 0.0));
            float valY = noise(movement + float3(0.0, 0.005, 0.0));
            float3 normal = normalize(float3(valX - val, valY - val, 0.08));

            float3 lightDir = normalize(float3(1.0, 1.0, 1.5));
            float spec = pow(max(dot(normal, lightDir), 0.0), 45.0);
            
            // Divine Mercury Aesthetics: Chrome with Violet Occlusion
            float3 shadowColor = float3(0.04, 0.01, 0.08); 
            float3 chromeColor = float3(0.92, 0.92, 0.98);   
            
            float3 finalColor = lerp(shadowColor, chromeColor, spec);
            
            // Add Magnetic Cyan Glow
            finalColor += float3(0.0, 0.5, 0.7) * uMagneticFlux * 0.4;
            
            // Environmental Ambient Lighting
            finalColor *= (0.2 + uLumen * 0.8);

            return half4(finalColor, 1.0);
        }
    """.trimIndent()
}
