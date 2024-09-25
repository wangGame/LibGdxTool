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
//    vec4 textureColor = v_color* texture2D(u_texture,v_textCoords);
//    float luminance = dot(textureColor.rgb, W);
//    gl_FragColor = vec4(vec3(luminance), textureColor.a)*vec4(223.0f/255.0,193.0f/255.0,171.0f/255.0,1.0);


    vec4 textureColor = v_color* texture2D(u_texture,v_textCoords);
//    float luminance = dot(textureColor.rgb, W);
    float v = (textureColor.r + textureColor.g+textureColor.b)/2.0f;
    gl_FragColor = vec4(vec3(v), textureColor.a)*vec4(203.0f/255.0,177.0f/255.0,161.0f/255.0,1.0);

}
