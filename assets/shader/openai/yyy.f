#ifdef GL_ES
precision mediump float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform sampler2D u_texture;
varying vec2 v_uv;

void main() {
    vec4 tex = texture2D(u_texture, v_uv);
    vec2 I = v_uv * u_resolution;
    vec4 O = vec4(0.0);
    for(float i=-1.0; i<1.0; i+=0.006) {
        vec2 r = u_resolution;
        vec2 p = cos(i*400.0 + u_time + vec2(0.0, 11.0)) * sqrt(1.0 - i*i);
        vec2 proj = (I + I - r)/r.y + vec2(p.x, i)/(p.y + 2.0);
        O += (cos(i + vec4(0.0,2.0,4.0,6.0)) + 1.0) * (1.0 - p.y) / dot(proj, proj) / 30000.0;
    }

    gl_FragColor = O;
    gl_FragColor.a = tex.a;
}
