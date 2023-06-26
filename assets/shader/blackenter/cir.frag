#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;
uniform float rataio;

void main() {
    vec2 temp =  v_textCoords;
    temp.y = v_textCoords.y * rataio;
    vec4 tempColor2 = v_color*texture2D(u_texture,temp);
//    gl_FragColor = tempColor2;
    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
    float xxx = (1.0-ss)  - 0.4 + time ;
    if(xxx<=0.0){/***/
        xxx = 0.0;
    }
    float a =  xxx;
    float value = 2.0;
    float power = 10.0;
    float powerValue = pow(value, -power);
    float powerScale = 1.0 / (1.0 - powerValue);
    float endValue = 0.5;
    if (a <= 0.5){
        endValue = (pow(value, power * (a * 2.0 - 1.0)) - powerValue) * powerScale / 2.0;
    } else{
        endValue = (2.0 - (pow(value, -power * (a * 2.0 - 1.0)) - powerValue) * powerScale) / 2.0;
    }
    gl_FragColor = vec4(v_color.rgb, (1.0-endValue));
}
