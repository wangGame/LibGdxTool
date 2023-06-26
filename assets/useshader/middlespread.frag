#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;
void main() {
    vec4 tempColor2 = v_color*texture2D(u_texture,v_textCoords);
    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
    float xxx = (1.0-ss)+1.0 - time;
    float fina = xxx * xxx *  xxx*  xxx*xxx;
    if(fina<0.001){
        discard;
    }else{
        gl_FragColor = vec4(v_color.rgb,fina);
    }
}