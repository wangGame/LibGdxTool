#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;
float hash(float n) {
    return fract(sin(n) * 43758.5453);
}

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;

    float t = u_time;

    float result = 0.0;

    for (int i = 0; i < 25; i++) {

        float fi = float(i);

        float spawnTime = floor(t - fi * 0.15);

        float life = t - spawnTime;

        float x = hash(spawnTime * 1.3 + fi);

        // 当前 y 位置
        float y = 1.0 - fract(life * 0.9);

        vec2 pos = vec2(x, y);

        // 当前像素到雨点位置
        float d = distance(uv, pos);

        // 👉 主雨滴
        float head = smoothstep(0.015, 0.0, d);

        // 👉 轨迹（关键）
        float trail = smoothstep(0.01, 0.0, abs(uv.x - x)) *
                      smoothstep(pos.y, pos.y + 0.3, uv.y);

        result += head + trail * 0.3;
    }

    gl_FragColor = vec4(vec3(result), 1.0);
}