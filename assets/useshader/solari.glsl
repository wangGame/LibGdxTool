#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
//"varying highp vec2 textureCoordinate;\n" +
varying vec2 v_textCoords;
//"uniform sampler2D inputImageTexture;\n" +
uniform sampler2D u_texture;
//"uniform highp float threshold;\n" +
uniform float time;
vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main() {
    vec4 textureColor = texture2D(u_texture, v_textCoords) * v_color;
    float luminance = dot(textureColor.rgb, W*W);
    float thresholdResult = step(luminance, time*0.3);

    vec3 finalColor = abs(thresholdResult - textureColor.rgb);
    gl_FragColor = vec4(finalColor, textureColor.w);
}