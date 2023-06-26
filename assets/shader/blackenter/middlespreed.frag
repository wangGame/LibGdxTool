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
////    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
////    float xxx = (1.0-ss)+0.8 - time;
//
////    float a =  xxx;
////    float value = 2.0;
////    float power = 8.0;
////    float powerValue = pow(value, -power);
////    float powerScale = 1.0 / (1.0 - powerValue);
////    float endValue = 1.0;
////    if (a <= 0.5){
////        endValue = (pow(value, power * (a * 2.0 - 1.0)) - powerValue) * powerScale / 2.0;
////    } else{
////        endValue = (2.0 - (pow(value, -power * (a * 2.0 - 1.0)) - powerValue) * powerScale) / 2.0;
////    }
//    gl_FragColor = tempColor2;
//    gl_FragColor = vec4(pow(textureColor.rgb, vec3(v_textCoords.x)), textureColor.w);
//}


#ifdef GL_ES
precision mediump float;
#endif


varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform float time;

void main() {
    vec4 textureColor = v_color * texture2D(u_texture,v_textCoords);
    vec4 textureColor2 = v_color * texture2D(u_texture2,v_textCoords);
    float ss = (textureColor.r + textureColor.g + textureColor.b) / 3.0;
    float xxx = (1.0-ss)+0.8 - time;
    float a =  xxx;
    float value = 2.0;
    float power = 9.0;
    float powerValue = pow(value, -power);
    float powerScale = 1.0 / (1.0 - powerValue);
    float endValue = 1.0;
    if (a <= 0.5){
        endValue = (pow(value, power * (a * 2.0 - 1.0)) - powerValue) * powerScale / 2.0;
    } else{
        endValue = (2.0 - (pow(value, -power * (a * 2.0 - 1.0)) - powerValue) * powerScale) / 2.0;
    }
    float tempTime = 0.35 + time * 0.1;
    if(time < 0.2f){
        tempTime = 0.35;
    }else{
        tempTime = 0.35 + time * 0.4 - 0.2;
    }

    if(tempTime > 1.0){
        tempTime = 1.0;
    }
    gl_FragColor = vec4(pow(textureColor2.rgb,vec3(tempTime)), (1.0-endValue)*textureColor2.a);

}