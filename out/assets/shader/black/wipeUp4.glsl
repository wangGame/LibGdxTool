//
//#ifdef GL_ES
//precision mediump float;
//#endif
//varying vec4 v_color;
//varying vec2 v_textCoords;
//uniform sampler2D u_texture;
//uniform sampler2D u_texture2;
//uniform float time;
//float OFFSET = 0.4;
//
//uniform float u;
//uniform float u2;
//uniform float v;
//uniform float v2;
//
//void main() {
//
//    vec2 temp_textCoord = vec2(v_textCoords.xy);
//    float vvv = (temp_textCoord.y - v) / (v2 - v);
//    float uuu = (temp_textCoord.x - u) / (u2 - u);
//    vec2 vecCoords = vec2(uuu,vvv);
//
//    vec4 sourceColor = texture2D(u_texture, v_textCoords) * v_color;
//    vec4 sourceColor2 = texture2D(u_texture2, vecCoords) * v_color;
//    float xx = (sourceColor2.r + sourceColor2.g + sourceColor2.b) / 3.0;
//    xx = xx + 1.0 - time;
//    if(xx<=0.0){
//        xx = 0.0;
//    }
//    xx = xx * xx * xx;
//    gl_FragColor = vec4(sourceColor.rgb,sourceColor.a * (1.0-xx));
//    //    gl_FragColor = mix(sourceColor2,sourceColor,0.1);
//}

//
#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform float time;
float OFFSET = 0.4;

uniform float u;
uniform float u2;
uniform float v;
uniform float v2;

void main() {

    vec2 temp_textCoord = vec2(v_textCoords.xy);
    float vvv = (temp_textCoord.y - v) / (v2 - v);
    float uuu = (temp_textCoord.x - u) / (u2 - u);
    vec2 vecCoords = vec2(uuu,vvv);

    vec4 textureColor2 = texture2D(u_texture, v_textCoords) * v_color;
    vec4 tempColor2 = texture2D(u_texture2, vecCoords) * v_color;
    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
    float xxx = (1.0 - ss)  - 0.4 + time;
    if(xxx<=0.0){
        xxx = 0.0;
    }
    float a =  xxx;
    float value = 2.0;
    float power =9.0;
    float powerValue = pow(value, -power);
    float powerScale = 1.0 / (1.0 - powerValue);
    float endValue = 0.0;
    if (a <= 0.5){
        endValue = (pow(value, power * (a * 2.0 - 1.0)) - powerValue) * powerScale / 2.0;
    } else{
        endValue = (2.0 - (pow(value, -power * (a * 2.0 - 1.0)) - powerValue) * powerScale) / 2.0;
    }

    float tempTime = 0.35 + time * 0.1;
    if(time < 0.2){
        tempTime = 0.35;
    }else{
        tempTime = 0.35 + time * 0.4 - 0.2;
    }

    if(tempTime > 1.0){
        tempTime = 1.0;
    }
    gl_FragColor = vec4(pow(textureColor2.rgb,vec3(tempTime)), endValue*textureColor2.a);
}
