#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5) * 100.0;
    p.x *= u_resolution.x / u_resolution.y;
    //中心点  step 在内就返回1，在外就返回0
    float d = length(p) ;


    float ring =
        smoothstep(30, 37, d);

    vec3 color = vec3(ring);
    gl_FragColor = vec4(color, 1.0);
}