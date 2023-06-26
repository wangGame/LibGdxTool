#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform float time;
const vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main() {
    vec4 textureColor2 = v_color * texture2D(u_texture2,v_textCoords);
    vec4 tempTexture = textureColor2;
    vec4 tempColor2 = v_color*texture2D(u_texture,v_textCoords);
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
