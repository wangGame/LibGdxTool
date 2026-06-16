#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;

    float t = u_time;


    vec2 dir = vec2(
        sin(uv.y * 10.0 + t),
        sin(uv.x * 10.0 + t)
    );

    vec2 distortUV = uv + dir * 0.05;

    float c = distortUV.x;

    gl_FragColor = vec4(vec3(c), 1.0);
}