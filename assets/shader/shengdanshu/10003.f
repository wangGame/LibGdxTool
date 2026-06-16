#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

void main() {
  vec2 uv = gl_FragCoord.xy / u_resolution;
  //gl_FragColor = vec4(uv.y,0.0, 0.0, 1.0);
    gl_FragColor = vec4(uv.y,uv.y, uv.y, 1.0);
}