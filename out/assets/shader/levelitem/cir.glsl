#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;
uniform float u;
uniform float v;
uniform float u2;
uniform float v2;

uniform sampler2D u_texture2;


void main() {
    vec4 textureColor = v_color * texture2D(u_texture,v_textCoords);
    float centerX = (u2 + u) / 2.0;
    float centerY = (v2 + v) / 2.0;
    float rrr = (u2 - u) / (v2 - v);
    float rad = (v_textCoords.x - centerX) * (v_textCoords.x - centerX)
    + (v_textCoords.y - centerY)*rrr * rrr*(v_textCoords.y - centerY);

    vec4 xnv =  v_color * texture2D(u_texture2,vec2(v_textCoords.x / (u2+u),v_textCoords.y / (v2+v)));

    if(rad < time * time){
        gl_FragColor = vec4(textureColor.rgb,xnv.a);
    }else{
        discard;
    }
}