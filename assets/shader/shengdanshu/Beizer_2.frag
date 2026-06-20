#ifdef GL_ES
precision highp float;
#endif

#define MAX_POINTS 20
#define SEGMENTS 100

uniform vec2 u_resolution;
uniform int u_pointCount;
uniform vec2 u_points[MAX_POINTS];

float drawPoint(vec2 p, vec2 c)
{
    return smoothstep(8.0, 0.0, length(p - c));
}

vec2 bezier2(vec2 p0, vec2 p1, vec2 p2, float t)
{
    float u = 1.0 - t;

    return
        u*u*p0 +
        2.0*u*t*p1 +
        t*t*p2;
}

void main()
{
    vec2 uv = gl_FragCoord.xy / u_resolution;

    vec2 p = (uv - 0.5) * u_resolution;

    vec3 color = vec3(0.0);

    //------------------------------------------------
    // 控制点
    //------------------------------------------------

    for(int i=0;i<MAX_POINTS;i++)
    {
        if(i>=u_pointCount)
            break;

        float d = drawPoint(p, u_points[i]);

        color += vec3(1.0,0.7,0.2) * d;
    }

    //------------------------------------------------
    // 贝塞尔
    //------------------------------------------------

    if(u_pointCount >= 3)
    {
        float minDist = 99999.0;

        for(int i=0;i<SEGMENTS;i++)
        {
            float t = float(i) / float(SEGMENTS-1);

            vec2 bp = bezier2(
                u_points[0],
                u_points[1],
                u_points[2],
                t
            );

            minDist = min(
                minDist,
                length(p - bp)
            );
        }

        float curve =
            smoothstep(
                3.0,
                0.0,
                minDist
            );

        color += vec3(0.2,0.8,1.0) * curve;
    }

    gl_FragColor = vec4(color,1.0);
}