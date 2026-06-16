#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5) ;
    p.x *= u_resolution.x / u_resolution.y;
    //中心点  step 在内就返回1，在外就返回0



    float d = length(p);
   // float glow = 1.0 / (d * 5.0 + 0.1);
   float glow = 1.0 / (d * d * 10.0 + 0.1);
   float t = sin(u_time * 2.0) * 0.5 + 0.5;
   float radius = 0.30 + t * 0.02;
    float ring =
        smoothstep(0.30, 0.31, radius + 0.01)
        - smoothstep(0.31, 0.32, radius+ 0.01);

    vec3 color = vec3(ring);
    color += glow;
    gl_FragColor = vec4(color, 1.0);
}