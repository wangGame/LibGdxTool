#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main()
{
    vec2 I = gl_FragCoord.xy;
    vec4 O = vec4(0.0);

    vec2 c;
    vec2 r = u_resolution;

    for (int i = 1; i <= 400; i++)
    {
        c = (I + I - r) / r.y * 300.0
            + vec2(0.0, float(i) - 230.0)
            + vec2(float(i + i % 99), float(i) / 4.0)
              * cos(u_time * 0.2 + vec2(float(i), float(i + 11))) * 0.5;

        vec4 col =
                ((i + int(u_time)) % 9 < 7)
                ? vec4(float(i % 2), 1.0 - float(i & 1), 0.0, 1.0)
                : vec4(1.0);

        O += col / dot(c, c);
    }

    gl_FragColor = sqrt(O / 2);
}