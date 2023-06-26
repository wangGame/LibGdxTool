#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;

uniform float width;
uniform float height;

void main() {
    vec4 pic = v_color * texture2D(u_texture, v_textCoords);
    float vvv = (pic.r + pic.g + pic.b) / 3.0;
    vec2 uv = v_textCoords;
    float ratio =   height / width;
    uv.x = uv.x-0.5;
    uv.y = uv.y* ratio -0.3;

    float sinV =0.93 - time;
    if(sinV<0.0){
        sinV = 0.0;
    }
    float r = sinV;
    float d = length(uv);



    float c = smoothstep(r,r-0.3,d);
    //    vec4 v= vec4(240.0F/255, 217.0F/255, 187.0F/255,vvv);
    gl_FragColor=vec4(245.0/255.0, 236.0/255.0, 208.0/255.0,c*pic.a);
}