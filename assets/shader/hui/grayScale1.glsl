#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float contrast;
vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main() {
        vec4 textureColor = vec4(1.0,1.0,1.0,1.0)*texture2D(u_texture,v_textCoords);
        float luminance = dot(textureColor.rgb, W);
        float a = luminance;
        a = a * (5.0 - 2.7 * 1.4648352);
        float xx = a;

        gl_FragColor = vec4(vec3(a),contrast*v_color.a);
//        float value = 2.0;
//        float power = 1.0;
//        float minV = pow(value,-power);
//        float scaleV = 1.0 / (1.0 - minV);
//        float endV = 1.0;
//
//        vec4 textureColor = vec4(1.0,1.0,1.0,1.0)*texture2D(u_texture,v_textCoords);
////        float a = dot(textureColor.rgb, W);
////        if (a <= 0.5) {
////                endV = (pow(value, power * (a * 2.0 - 1.0)) - minV) * scaleV / 2.0;
////        } else{
////                endV =  (2.0 - (pow(value, -power * (a * 2.0 - 1.0)) - minV) * scaleV) / 2.0;
////        }
//        gl_FragColor = vec4(,0.5);
}