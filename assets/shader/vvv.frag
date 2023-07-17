#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;


void main() {
     normal = texture2D(u_texture, v_textCoords);
     gl_FragColor = v_color * normal;
}