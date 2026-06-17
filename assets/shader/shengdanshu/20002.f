#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

const float PI = 3.1415926;

void main()
{
    vec2 uv = gl_FragCoord.xy / u_resolution.xy;

    vec2 p = uv - 0.5;

    p.x *= u_resolution.x / u_resolution.y;

    float r = length(p);
    float a = atan(p.y, p.x);

    a += u_time * 0.3;
    a += 0.1 / (r + 0.1);
    float sector = PI / 6.0;

    a = abs(mod(a, sector * 2.0) - sector);

  //  float v = sin(a * 10.0 + r * 20.0 - u_time * 2.0);
    float v =
        sin(a * 8.0 + u_time)
        + sin(r * 30.0 - u_time * 3.0);

    vec3 color = 0.5 + 0.5 * cos(
        vec3(0.0, 2.0, 4.0) + v
    );

    gl_FragColor = vec4(color, 1.0);
}