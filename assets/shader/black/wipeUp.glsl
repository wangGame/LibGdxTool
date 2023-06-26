
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
    vec4 textureColor = texture2D(u_texture2, vecCoords) * v_color;
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
    if(time < 0.2){
        tempTime = 0.35;
    }else{
        tempTime = 0.35 + time * 0.4 - 0.2;
    }

    if(tempTime > 1.0){
        tempTime = 1.0;
    }
    gl_FragColor = vec4(pow(textureColor2.rgb,vec3(tempTime)), (1.0-endValue)*textureColor2.a);

    //    xx = xx * xx * xx;
//    xx = xx - 0.5 + time;
//
//    float vvv1 = 0.22;
//
//    float minusv = vvv1 - time*0.2 + 0.2;
//
//    if(time<1.0){
//        minusv = 0.22;
//    }
//
//    if(minusv > 0.0){
//        sourceColor.r += minusv;
//        sourceColor.g += minusv;
//        sourceColor.b += minusv;
//    }
//    sourceColor.r = min(1.0,sourceColor.r);
//    sourceColor.g = min(1.0,sourceColor.g);
//    sourceColor.b = min(1.0,sourceColor.b);
//
//    if(xx >= 1.0){
//        xx = 1.0;
//    }
//    float powNum = 0.5;
//    powNum+=time;
//    if(powNum>=1.0){
//        powNum = 1.0;
//    }
//    vec3 temVec3 = vec3(powNum);
//    temVec3 = pow(sourceColor.rgb,temVec3);
//    gl_FragColor = vec4(temVec3,sourceColor.a * xx);
//        gl_FragColor = vec4(sourceColor.rgb,sourceColor.a * xx);
//    gl_FragColor = vec4(sourceColor.rgb,sourceColor.a * xx);
}