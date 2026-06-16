#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;
void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;

    float t = u_time;

    vec2 dir1 = vec2(sin(uv.y * 6.0 + t), cos(uv.x * 6.0 + t));
    vec2 dir2 = vec2(cos(uv.y * 3.0 - t), sin(uv.x * 3.0 - t));

    vec2 flow = (dir1 + dir2) * 0.5;

    vec2 p = uv + flow * 0.1;

    float v = sin(p.x * 15.0 + p.y * 15.0 + t * 2.0);

    float mask = smoothstep(0.2, 0.8, v);

    vec3 color = vec3(0.1, 0.7, 1.2) * mask;

    gl_FragColor = vec4(color, 1.0);
}