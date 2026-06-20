#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;

void main() {
    vec2 colorV2 = gl_FragCoord.xy / u_resolution;
    gl_FragColor = vec4(colorV2, 1.0, 1.0);
}