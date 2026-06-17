#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float hash(vec2 p)
{
    return fract(
        sin(dot(p, vec2(127.1,311.7)))
        * 43758.5453123
    );
}

float noise(vec2 p)
{
    vec2 i = floor(p);
    vec2 f = fract(p);

    float a = hash(i);
    float b = hash(i + vec2(1.0,0.0));
    float c = hash(i + vec2(0.0,1.0));
    float d = hash(i + vec2(1.0,1.0));

    f = f*f*(3.0-2.0*f);

    return mix(
        mix(a,b,f.x),
        mix(c,d,f.x),
        f.y
    );
}

void main()
{
    vec2 uv =
        (gl_FragCoord.xy * 2.0
        - u_resolution.xy)
        / min(u_resolution.x,
              u_resolution.y);

    float r = length(uv);
    float a = atan(uv.y, uv.x);

    //---------------------------------
    // 水波
    //---------------------------------

    float wave =
        sin(r * 18.0
        - u_time * 3.0);

    wave *= exp(-r * 2.5);

    //---------------------------------
    // 龙纹
    //---------------------------------

    float dragon =
        sin(a * 6.0
        + u_time
        + r * 10.0);

    dragon =
        smoothstep(
            0.4,
            0.8,
            dragon
        );

    dragon *=
        exp(-r * 1.2);

    //---------------------------------
    // 发光环
    //---------------------------------

    float ring =
        smoothstep(
            0.25,
            0.23,
            abs(r - 0.5)
        );

    //---------------------------------
    // 太极旋转
    //---------------------------------

    float yinYang =
        sin(a * 2.0
        + u_time * 0.5);

    yinYang =
        smoothstep(
            0.0,
            0.1,
            yinYang
        );

    //---------------------------------
    // 粒子星光
    //---------------------------------

    vec2 p = uv * 6.0;

    float n =
        noise(
            p
            + u_time * 0.2
        );

    float spark =
        smoothstep(
            0.95,
            1.0,
            n
        );

    //---------------------------------
    // 配色
    //---------------------------------

    vec3 bg =
        vec3(
            0.02,
            0.05,
            0.08
        );

    vec3 jade =
        vec3(
            0.10,
            0.85,
            0.70
        );

    vec3 gold =
        vec3(
            1.0,
            0.85,
            0.35
        );

    vec3 col = bg;

    col += jade * wave * 0.3;
    col += jade * dragon;
    col += gold * ring;
    col += jade * yinYang * 0.2;
    col += gold * spark;

    //---------------------------------
    // 中央发光
    //---------------------------------

    col +=
        0.05 / (r + 0.03);

    //---------------------------------
    // 暗角
    //---------------------------------

    col *=
        smoothstep(
            1.5,
            0.2,
            r
        );

    gl_FragColor =
        vec4(col,1.0);
}