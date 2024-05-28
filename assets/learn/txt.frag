#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;

uniform sampler2D u_texture1;
uniform float ratw;
uniform float rath;


void main() {
    vec2 temp = vec2(v_textCoords.x * ratw,v_textCoords.y * rath);
    vec4 pic1 = v_color * texture2D(u_texture1, temp);
    vec4 pic = v_color * texture2D(u_texture, v_textCoords);
    if(pic.a <=0.0){
        discard;
    }else{
        gl_FragColor = pic1;
    }
}