
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;

void main() {
    vec4 textureColor = v_color * texture2D(u_texture,v_textCoords);
    gl_FragColor = textureColor;
}


