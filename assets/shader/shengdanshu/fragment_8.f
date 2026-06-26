#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;

void main() {
    vec2 colorV2 = gl_FragCoord.xy / u_resolution;
    colorV2 = colorV2 * 200.0-100.0;
    colorV2.x *= u_resolution.x / u_resolution.y;
    float len = length(colorV2);
    vec3 color = 0.5 + 0.5 * vec3(sin(len));
    gl_FragColor = vec4(color, 1.0);
}