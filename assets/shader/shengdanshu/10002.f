#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

// ----------------------
// hash noise
// ----------------------
float hash(vec2 p) {
return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

// ----------------------
// smooth noise
// ----------------------
float noise(vec2 p) {
vec2 i = floor(p);
vec2 f = fract(p);

float a = hash(i);
float b = hash(i + vec2(1.0, 0.0));
float c = hash(i + vec2(0.0, 1.0));
float d = hash(i + vec2(1.0, 1.0));

vec2 u = f * f * (3.0 - 2.0 * f);

return mix(a, b, u.x) +
(c - a) * u.y * (1.0 - u.x) +
(d - b) * u.x * u.y;
}

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;


    vec2 p = uv - vec2(0.5, 0.0);

    p.x *= u_resolution.x / u_resolution.y;


    p.y *= 2.0;


    float t = u_time * 0.8;


    float n = noise(vec2(p.x * 3.0, p.y * 3.0 - t));


    float shape = 1.0 - p.y;

    // ❗ 火焰核心
    float fire = n * shape;

    // 🔥 底部更亮（热源）
    float core = smoothstep(0.0, 0.4, shape);

    fire += core * 0.6;

    // ❌ 去掉顶部
    fire *= smoothstep(1.0, 0.0, p.y);

    // 🎨 火焰颜色
    vec3 color = vec3(0.0);

    // 红 → 黄 → 白
    color += fire * vec3(1.0, 0.4, 0.1);
    color += fire * fire * vec3(1.0, 0.8, 0.2);

    gl_FragColor = vec4(color, 1.0);
}