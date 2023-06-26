#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
uniform vec4 v_color1;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform float time;
void main() {
    vec4 tempColor1 = texture2D(u_texture,v_textCoords);
    vec4 tempColor2 = texture2D(u_texture2,v_textCoords);
    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
    vec4 xcc = vec4(1.0,1.0,1.0,ss-1.0);
    vec4 cccc =  xcc;
    cccc.a += clamp(time,0.0,1.0);
    //    cccc.a = 1-cccc.a;
    gl_FragColor = tempColor1* cccc;
}