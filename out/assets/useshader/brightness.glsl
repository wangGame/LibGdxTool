#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float ccc;

void main() {
    float tempValue = ccc;
    vec4 textureColor = v_color* texture2D(u_texture,v_textCoords);
//    gl_FragColor = vec4(clamp(textureColor.rgb+vec3(1.5*sin(contrast)),0.0,1.0), textureColor.w);
    tempValue = 1.0 - tempValue;
    if(tempValue < 0.0){
        gl_FragColor = vec4(pow(textureColor.rgb, vec3(0)), textureColor.w);
    }else{
        gl_FragColor = vec4(pow(textureColor.rgb, vec3(tempValue)), textureColor.w);
    }
//    gl_FragColor = vec4(pow(textureColor.rgb, vec3(1)), textureColor.w);
}