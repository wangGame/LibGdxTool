#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;

void main() {
    vec2 colorV2 = gl_FragCoord.xy / u_resolution;
    colorV2 = colorV2 * 2.0-0.2;
    colorV2.x *= u_resolution.x / u_resolution.y;
    float len = length(colorV2);
    float circle = step(len,0.5);
    vec3 color = vec3(circle);
    gl_FragColor = vec4(color, 1.0);
}