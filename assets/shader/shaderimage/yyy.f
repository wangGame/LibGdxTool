#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;
uniform vec2 u_resolution;
varying vec4 v_color;
varying vec2 v_uv;
void main() {

    vec4 tex = texture2D(u_texture, v_uv);

    // === Image 局部像素坐标 ===
    vec2 I = v_uv * u_resolution;

    // === Y 轴翻转（关键） ===
    I.y = u_resolution.y - I.y;

    // === 居中 ===
    I -= u_resolution * 0.5;

    vec4 O = vec4(0.0);
    vec2 c;
    vec2 r = u_resolution;

    for (int i = 1; i <= 400; i++)
    {
        c = (I + I) / r.y * 400.0
            + vec2(0.0, float(i) - 300.0)
            + vec2(float(i + i % 100), float(i) / 4.0)
              * cos(u_time * 0.2 + vec2(float(i), float(i + 11))) * 0.5;

        vec4 col =
                ((i + int(u_time)) % 2 < 2)
                ? vec4(float(i % 2), 1.0 - float(i & 1), 0.0, 1.0)
                : vec4(1.0);

        O += col / dot(c, c);
    }

    vec4 color = sqrt(O / 2.0);
    gl_FragColor = vec4(color.rgb, tex.a);
}
