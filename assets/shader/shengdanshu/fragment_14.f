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

    //呼吸动画
    float t = sin(u_time * 2.0) * 0.5 + 0.5;
    float radius = 0.30 + t * 0.03;

    // 🔥 圆环
    float ring =
        smoothstep(radius, radius + 0.01, d)
      - smoothstep(radius + 0.01, radius + 0.02, d);

    // ✨ glow（核心）
    float glow = 1.0 / (d * d * 8.0 + 0.1);

    // 🎨 颜色叠加
    vec3 color = vec3(0.0);

    // 蓝色能量环
    color += ring * vec3(0.2, 0.6, 1.0);

    // 外扩光晕
    color += glow * 0.125;

    gl_FragColor = vec4(color, 1.0);
}