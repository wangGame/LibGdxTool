#ifdef GL_ES
precision mediump float;
#endif


varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;

void main() {
    vec4 textureColor = v_color * texture2D(u_texture,v_textCoords);
    float ss = (textureColor.r + textureColor.g + textureColor.b) / 3.0;
    float xxx = (1.0-ss)+0.8 - time;
    float a =  xxx;
    float value = 2.0;
    float power = 4.0;
    float powerValue = pow(value, -power);
    float powerScale = 1.0 / (1.0 - powerValue);
    float endValue = 1.0;
    if (a <= 0.5){
        endValue = (pow(value, power * (a * 2.0 - 1.0)) - powerValue) * powerScale / 2.0;
    } else{
        endValue = (2.0 - (pow(value, -power * (a * 2.0 - 1.0)) - powerValue) * powerScale) / 2.0;
    }

//    gl_FragColor = vec4(v_color.rgb,endValue);
    gl_FragColor = vec4(v_color.rgb, endValue);
}