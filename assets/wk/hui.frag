#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float rr;
uniform float posx;
uniform float posy;

void main() {
    vec4 tempColor = v_color * texture2D(u_texture,v_textCoords);
    if((( v_textCoords.x - rr + posx) * ( v_textCoords.x - rr + posx) +
            ( v_textCoords.y - rr + posy) * ( v_textCoords.y - rr+ posy))<rr*rr){
        gl_FragColor = tempColor;
        gl_FragColor.a = 0;
    }else{
        gl_FragColor = tempColor;
    }
}