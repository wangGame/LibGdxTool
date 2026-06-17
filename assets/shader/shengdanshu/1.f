#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float rect(vec2 p, vec2 center, vec2 size) {
    vec2 d = abs(p - center) - size * 0.5;
    float distanceToRect = max(d.x, d.y);

    float aa = 1.5 / u_resolution.y;
    return 1.0 - smoothstep(-aa, aa, distanceToRect);
}

void main() {
    vec2 p = (gl_FragCoord.xy - u_resolution * 0.5)
           / u_resolution.y;

    float word = 0.0;

    // 外框
    word = max(word, rect(p, vec2(0.0,  0.20), vec2(0.42, 0.055)));
    word = max(word, rect(p, vec2(0.0, -0.20), vec2(0.42, 0.055)));
    word = max(word, rect(p, vec2(-0.18, 0.0), vec2(0.055, 0.44)));
    word = max(word, rect(p, vec2( 0.18, 0.0), vec2(0.055, 0.44)));

    // 中间竖线
    word = max(word, rect(p, vec2(0.0, 0.0), vec2(0.055, 0.65)));

    vec3 backgroundColor = vec3(0.06, 0.08, 0.12);
    vec3 textColor = vec3(1.0, 0.25, 0.1);

    vec3 color = mix(backgroundColor, textColor, word);

    gl_FragColor = vec4(color, 1.0);
}