#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main()
{
    vec2 uv =
        gl_FragCoord.xy
        / u_resolution.xy;

    vec2 p = uv - 0.5;

    p.x *=
        u_resolution.x
        / u_resolution.y;

    float r = length(p);

    float a =
        atan(p.y,p.x);

    // 黑洞扭曲
    a += 0.6 / (r + 0.05);

    // 旋转
    a += u_time * 0.3;


    vec3 color = 0.5+0.5*vec3(sin(a));


    gl_FragColor =
        vec4(color,1.0);
}