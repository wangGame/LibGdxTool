#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;
    float n = hash(uv + u_time * 0.5);
    vec3 color = vec3(n);
    gl_FragColor = vec4(color, 1.0);
}