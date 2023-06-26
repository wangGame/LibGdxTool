#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;

uniform float u;
uniform float u2;
uniform float v;
uniform float v2;

void main() {
    float vm = abs(v2 + v);
    float um = abs(u2 + u);
    vec4 pic = v_color * texture2D(u_texture, v_textCoords);

    vec2 uv = v_textCoords;
    uv.x = uv.x-um/2.0;
    uv.y = uv.y-vm/2.0;


    //time xi hua
    float sinV =max(vm,um)/2.0 - time*0.4;
    if(sinV<0.0){
        sinV = 0.0;
    }
    float r = sinV;
    float d = length(uv);


    float endMax = max(u2-u,v2-v);

    float c = smoothstep(r,r-endMax*0.25,d);
    vec4 v= pic;
    gl_FragColor=vec4(vec3(v.rgb),(1.0-c)*v.a);
}

//#ifdef GL_ES
//precision mediump float;
//#endif
//
//
////input from vertex shader
//varying vec4 v_color;
//varying vec2 v_textCoords;
//uniform sampler2D u_texture;
//uniform float time;
//void main() {
//    vec4 tempColor2 = v_color*texture2D(u_texture,v_textCoords);
//    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
//    float xxx = (1.0-ss)+1.0 - time;
//    float fina = xxx * xxx *  xxx*  xxx*xxx;
////    if(fina<0.001){
////        discard;
////    }else{
//        gl_FragColor = vec4(tempColor2.rgb,tempColor2.a * (1-fina));
////    }
//}