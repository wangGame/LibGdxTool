#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;

void main() {
    vec2 colorV2 = gl_FragCoord.xy / u_resolution;
    colorV2.x *= u_resolution.x / u_resolution.y;
    gl_FragColor = vec4(colorV2, 1.0, 1.0);
}