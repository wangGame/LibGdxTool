#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;

varying vec2 v_texCoord0;
varying vec4 v_color;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0);
    float alpha = smoothstep(0.0, 1.0, 1.0 - u_time);
    gl_FragColor = vec4(color.rgb, color.a * alpha) * v_color;
}
