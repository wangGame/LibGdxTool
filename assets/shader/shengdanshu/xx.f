#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float f_sin(float x){ return sin(x); }
float f_cos(float x){ return cos(x); }
float f_x2(float x){ return x * x * 0.3; }
float f_abs(float x){ return abs(x); }

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5) * 4.0;


    // 📊 坐标轴
    float axisX = smoothstep(0.01, 0.0, abs(p.y));
    float axisY = smoothstep(0.01, 0.0, abs(p.x));

    p.x *= u_resolution.x / u_resolution.y;

    float x = p.x * 2.0;

    float w = fwidth(p.y);

    float dist1 = abs(p.y - f_sin(x));
    float dist2 = abs(p.y - f_cos(x));
    float dist3 = abs(p.y - f_x2(x));
    float dist4 = abs(p.y - f_abs(x));

    float line1 = smoothstep(w * 1.5, 0.0, dist1);
    float line2 = smoothstep(w * 1.5, 0.0, dist2);
    float line3 = smoothstep(w * 1.5, 0.0, dist3);
    float line4 = smoothstep(w * 1.5, 0.0, dist4);

    vec3 color = vec3(0.0);

    // sin - 蓝色
    color += vec3(0.4, 0.7, 1.5) * line1;

    // cos - 绿色
    color += vec3(0.4, 1.2, 0.6) * line2;

    // x^2 - 红色
    color += vec3(1.5, 0.4, 0.4) * line3;

    // |x| - 黄色
    color += vec3(1.5, 1.2, 0.3) * line4;

    color += vec3(1.0) * (axisX + axisY);

    gl_FragColor = vec4(color, 1.0);
}