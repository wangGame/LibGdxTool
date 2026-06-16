#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

uniform float a;

// 🎛 参数（你可以先写死测试，后面可以接 UI）
//float a = 1.0;
float b = 0.0;
float c = 0.0;

// 📈 二次函数
float f(float x){
    return a * x * x + b * x + c;
}

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;

    // 📐 坐标系统
    vec2 p = (uv - 0.5) * 4.0;
    p.x *= u_resolution.x / u_resolution.y;

    float x = p.x;

    float y = f(x);

    // 📏 距离（函数线）
    float dist = abs(p.y - y);

    // 🔥 抗锯齿（关键）
    float w = fwidth(p.y);
    float line = smoothstep(w * 1.5, 0.0, dist);

    // 🎨 颜色
    vec3 color = vec3(0.0);

    // 蓝色抛物线
    color += vec3(0.4, 0.7, 1.5) * line;

    gl_FragColor = vec4(color, 1.0);
}