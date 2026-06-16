#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float f(float x){
    return sin(x);
}

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5) * 4.0;
    p.x *= u_resolution.x / u_resolution.y;

    // 📊 坐标轴
    float axisX = smoothstep(0.01, 0.0, abs(p.y));
    float axisY = smoothstep(0.01, 0.0, abs(p.x));

    // 📈 函数
    float y = f(p.x);

    float dist = abs(p.y - y);

    float w = fwidth(p.y);

    float line = smoothstep(w * 1.5, 0.0, dist);

    vec3 color = vec3(0.0);

    // 坐标轴（灰色）
    color += vec3(0.3) * (axisX + axisY);

    // 函数曲线（蓝色）
    color += vec3(0.6, 0.8, 1.5) * line;

    gl_FragColor = vec4(color, 1.0);
}