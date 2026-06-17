#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main()
{
    vec2 uv = gl_FragCoord.xy / u_resolution;

    vec2 p = uv - 0.5;

    p.x *= u_resolution.x / u_resolution.y;

    // Domain Warp
    p += 0.5 * sin(
        p.yx * 8.0
        + u_time
    );

    float d = length(p);

    float v = sin(
        d * 30.0
        - u_time * 3.0
    );

    vec3 color =
        0.5 + 0.5 * cos(
            vec3(0.0,2.0,4.0)
            + v
        );

    gl_FragColor =
        vec4(color,1.0);
}