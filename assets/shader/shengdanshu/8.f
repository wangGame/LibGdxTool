
#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = uv - 0.5;

    p.x *= u_resolution.x / u_resolution.y;

    float d = length(p);


    float t = u_time * 0.8;

    // 🔥 冲击波半径
    float radius = t;

    // 💥 波环
    float wave =
        smoothstep(radius, radius + 0.02, d)
      - smoothstep(radius + 0.02, radius + 0.04, d);

    // ✨ 衰减（越远越淡）
    float fade = 1.0 - smoothstep(0.0, 1.5, d);

    // 🎨 颜色
    vec3 color = vec3(0.0);

    color += wave * vec3(0.2, 0.6, 1.0);
    color *= fade;

    gl_FragColor = vec4(color, 1.0);
}