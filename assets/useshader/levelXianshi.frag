#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform vec2 vscale;
uniform float time;
void main() {
    vec4 tempColor = v_color*texture2D(u_texture, v_textCoords);
    vec2 tempCoords = v_textCoords;
    tempCoords.x *= vscale.x;
    tempCoords.y *= vscale.y;
    vec4 tempColor2 =   texture2D(u_texture2, tempCoords);
    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
    vec4 xcc = vec4(1.0, 1.0, 1.0, ss+1);
    vec4 cccc = xcc;
    cccc.a -= clamp(2*abs(sin(time*2.0)),0.0,1.0);
    gl_FragColor = vec4(tempColor.rgb,tempColor.a * cccc.a); //+ tempHuang;
//        gl_FragColor = vec4(vec3(1),tempColor.a * v_textCoords1.x); //+ tempHuang;
//          gl_FragColor = mix(tempColor,tempColor2,0.7);
}