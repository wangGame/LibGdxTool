#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform float contrast;
uniform float u;
uniform float v;
uniform float u2;
uniform float v2;
vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main() {
    vec4 textureColor = vec4(1.0,1.0,1.0,1.0)*texture2D(u_texture,v_textCoords);

    vec4 xnv =  v_color * texture2D(u_texture2,v_textCoords);

    gl_FragColor = textureColor;
    gl_FragColor.a = xnv.a;
//    float rr = 0.07;
//    if(v_textCoords.x < rr && v_textCoords.y < rr){
//        if((v_textCoords.x - rr) * (v_textCoords.x - rr) + (v_textCoords.y - rr) * (v_textCoords.y - rr) < rr * rr){
//            gl_FragColor = textureColor;
//        }else{
//            discard;
//        }
//    }else if(v_textCoords.x > (1.0-rr)&&v_textCoords.y<rr){
//        if((1.0-v_textCoords.x - rr) * (1.0-v_textCoords.x - rr) + (v_textCoords.y - rr) * (v_textCoords.y - rr) < rr * rr){
//            gl_FragColor = textureColor;
//        }else{
//            discard;
//        }
//    }else if(v_textCoords.x < rr&&v_textCoords.y>(1.0-rr)){
//        if((v_textCoords.x - rr) * (v_textCoords.x - rr) + (1.0-v_textCoords.y - rr) * (1.0-v_textCoords.y - rr) < rr * rr){
//            gl_FragColor = textureColor;
//        }else{
//            discard;
//        }
//    }else if(v_textCoords.x > (1.0-rr)&&v_textCoords.y>(1.0-rr)){
//        if((1.0-v_textCoords.x - rr) * (1.0-v_textCoords.x - rr) + (1.0-v_textCoords.y - rr) * (1.0-v_textCoords.y - rr) < rr * rr){
//            gl_FragColor = textureColor;
//        }else{
//            discard;
//        }
//    }else  {
//        gl_FragColor = textureColor;
//    }

//    gl_FragColor = vec4(vec3(a),0.5 * v_color.a);
}