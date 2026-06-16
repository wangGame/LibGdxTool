#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5f) * 100.0;
    p.x *= u_resolution.x / u_resolution.y;
    vec3 color = vec3(0.0);
    float d = length(p);
    //color big zero
    color += 0.5 + 0.5 * cos(u_time + d * 10f);
    gl_FragColor = vec4(color, 1.0);
}