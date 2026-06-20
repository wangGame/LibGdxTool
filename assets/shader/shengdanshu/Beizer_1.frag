#ifdef GL_ES
precision highp float;
#endif

#define MAX_POINTS 20

uniform vec2 u_resolution;
uniform int u_pointCount;
uniform vec2 u_points[MAX_POINTS];

float drawPoint(vec2 p, vec2 c) {
return smoothstep(8.0, 0.0, length(p - c));
}

void main() {

    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5) * u_resolution;

    vec3 color = vec3(0.0);

    for(int i = 0; i < MAX_POINTS; i++) {
        if(i >= u_pointCount) break;

        float d = drawPoint(p, u_points[i]);
        color += vec3(1.0, 0.7, 0.2) * d;
    }

    gl_FragColor = vec4(color, 1.0);
}