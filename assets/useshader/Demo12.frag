#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform float time;

uniform float u;
uniform float u2;
uniform float v;
uniform float v2;

void main() {
    vec4 tempColor = v_color*texture2D(u_texture, v_textCoords);

    float u = (v_textCoords.x - u)/abs(u2 - u);
    float v = (v_textCoords.y - v)/abs(v2 - v);
    vec2 tempCoords = vec2(u,v);
    vec4 tempColor2 = vec4(1.0,1.0,1.0,1) * texture2D(u_texture2, tempCoords);
    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
    vec4 xcc = vec4(1.0, 1.0, 1.0, ss-0.5);
    vec4 cccc = xcc;
    cccc.a += 2.0*abs(time);
    float endA = tempColor.a * cccc.a;
    endA = clamp(endA,0.0,1.0);
    float endAddColor = 0.2-time*0.6;
    if(time>=0.6 && endAddColor <= 0.0){
        gl_FragColor = tempColor;
    }else{
        if(endAddColor<=0.0){
            endAddColor = 0.0;
        }
        gl_FragColor = vec4(clamp(tempColor.rgb+vec3(endAddColor),0.0,1.0),endA*v_color);
    }
}