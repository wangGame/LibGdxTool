#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;

vec4 clam(vec4 ve){
    if(ve.r > 1.0){
        ve.r = 1.0;
    }
    if(ve.g > 1.0){
        ve.g = 1.0;
    }
    if(ve.b > 1.0){
        ve.b = 1.0;
    }
    return ve;
}

void main() {
    float cc = 1.70354009 - time ;
    vec4 textureColor = v_color* texture2D(u_texture,v_textCoords);
    vec4 ccc  = vec4(textureColor.rgb * pow(2.0, cc), textureColor.w);
    if(ccc.r + ccc.g + ccc.b > 3.7){
        discard;
    }else{
        gl_FragColor = textureColor;
    }
    //    float tt = 2.0 - time;
    //    vec4 textureColor = v_color* texture2D(u_texture,v_textCoords);
    //    vec4 end = vec4(textureColor.rgb * pow(2.0, tt), textureColor.w);
    //
    //    float rrr = (end.r + end.g + end.b) / 6.0;
    //
    //    if(rrr<0.65){
    //        gl_FragColor = vec4(textureColor.rgb,(rrr));
    //    }else{
    //        discard;
    //    }
}