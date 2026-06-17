#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

vec2 rotate(vec2 p, float a)
{
    float s = sin(a);
    float c = cos(a);

    return vec2(
        c * p.x - s * p.y,
        s * p.x + c * p.y
    );
}

void main()
{
    vec2 uv = gl_FragCoord.xy / u_resolution;

    vec2 p = uv - 0.5;
    p.x *= u_resolution.x / u_resolution.y;

    // 旋转空间
    p = rotate(p, u_time);

    float v = sin(p.x * 7.0);
    float h = cos(p.y * 5.0);

    vec3 color = vec3(
        0.5 + 0.5 * v
    );

    color += vec3(
        0.5 + 0.5 * h
    );

    gl_FragColor = vec4(color, 1.0);
}