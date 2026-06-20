#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

// ------------------
// hash 随机函数
// ------------------
float hash(vec2 p) {
return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;

    vec3 color = vec3(0.0);

    // 🌧️ 网格密度（雨点数量）
    vec2 grid = uv * 6.0;
    vec2 id = floor(grid);

    // ======================================================
    // 遍历邻居格子（避免边界断裂）
    // ======================================================
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {

            vec2 cell = id + vec2(x, y);

            // 🎯 随机中心点（0~1）
            vec2 center = vec2(
                    hash(cell),
                    hash(cell + 10.0)
            );

            // ⏱ 每个雨点的出生时间
            float startTime = hash(cell + 20.0) * 5.0;

            float age = u_time - startTime;

            // ❌ 未出生直接跳过
            if (age < 0.0) continue;

            float radius = age * 0.6;

            // 📍 当前格子空间坐标
            vec2 cellPos = vec2(x, y);


            vec2 worldPos = cell + center;


            float d = length(grid - worldPos);


            float wave =
                    smoothstep(radius, radius + 0.02, d)
                    - smoothstep(radius + 0.02, radius + 0.04, d);


            float fade = 1.0 - smoothstep(0.0, 3.0, age);

            // 🎨 颜色
            vec3 col = vec3(0.2, 0.6, 1.0);

            color += wave * col * fade;
        }
    }
    {

        vec2 center = vec2(0.1, 0.5);
        vec2 p = uv - center;
        float d = length(p) ;
        float circle = step(d,0.3) * 0.2;
        color += vec3(circle);
    }
    gl_FragColor = vec4(color, 1.0);
}