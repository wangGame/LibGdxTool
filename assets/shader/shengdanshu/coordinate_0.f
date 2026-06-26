#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5) * 4.0;

    // 📊 坐标轴
    float axisX = smoothstep(0.01, 0.0, abs(p.y));
    float axisY = smoothstep(0.01, 0.0, abs(p.x));

    vec3 color = vec3(0.0);
    color += vec3(1.0) * (axisX + axisY);

    gl_FragColor = vec4(color, 1.0);
}