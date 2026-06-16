#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;
void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;

    vec2 p = (uv - 0.5) * 4.0;
    p.x *= u_resolution.x / u_resolution.y;

    float x = p.x * 6.2831;
    float y = sin(x);

    float dist = abs(p.y - y);

    float w = fwidth(p.y);   // 🔥关键修复

    float line = smoothstep(w * 1.5, 0.0, dist);

    float glow = smoothstep(w * 6.0, 0.0, dist);

    vec3 color =
        vec3(0.6, 0.8, 1.5) * glow +
        vec3(1.0) * line;

    gl_FragColor = vec4(color, 1.0);
}