#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;

void main() {
    vec4 tempColor = v_color*texture2D(u_texture, v_textCoords);
    float color1 = (tempColor.r + tempColor.g+tempColor.b) / 3.0f;
    gl_FragColor = vec4(1,1,1,color1);


}