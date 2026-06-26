#ifdef GL_ES
precision highp float;
#endif

void main() {
    vec2 colorV2 = gl_FragCoord.xy;
    gl_FragColor = vec4(normalize(colorV2), 1.0, 1.0);
}