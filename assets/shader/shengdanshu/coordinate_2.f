#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float f_sin(float x){ return sin(x); }
float f_cos(float y){ return cos(y); }
float f_xsquartx(float x){ return x * x; }

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;

    vec2 p = vec2(0.0);
    p.x = (uv.x - 0.5) * 720.0 * (u_resolution.x / u_resolution.y);
    p.y = (uv.y - 0.5) * 1280.0;

    // 坐标轴
    float axisX = smoothstep(5.0 * (u_resolution.x / u_resolution.y), 0.0, abs(p.y));
    float axisY = smoothstep(5.0, 0.0, abs(p.x));

    float x = p.x;

    float amplitude = 200.0;
    float wave1 = f_sin(0.01 * x) * amplitude;
    float wave2 = f_cos(0.01 * x) * amplitude;
    float wave3 = f_xsquartx(0.01 * x) * amplitude;


    float dist1 = abs(p.y - wave1);
    float dist2 = abs(p.y - wave2);
    float dist3 = abs(p.y - wave3);

    float w = fwidth(p.y);
    float line1 = smoothstep(w, 0.0, dist1);
    float line2 = smoothstep(w, 0.0, dist2);
    float line3 = smoothstep(w, 0.0, dist3);

    vec3 color = vec3(0.0);
    color += vec3(1.0) * (axisX + axisY);
    color += vec3(0.4, 0.7, 1.5) * line1;
    color += vec3(1.0,0.0,0.0) * line2;
    color += vec3(0.0,1.0,0.0) * line3;
    gl_FragColor = vec4(color, 1.0);
}