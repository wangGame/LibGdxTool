#ifdef GL_ES

#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;

uniform float wh_ratio;


void main() {
    vec4 textColor = textureLod(u_texture, v_textCoords,0.4) * v_color;
    gl_FragColor = textColor;

}