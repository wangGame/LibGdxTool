//#ifdef GL_ES
//precision mediump float;
//#endif
//
//varying vec4 v_color;
//varying vec2 v_textCoords;
//
//uniform sampler2D u_texture;
//uniform float u_bottomFade;
//uniform float u_topFade;
//uniform float top;
//
//void main() {
//    vec4 textureColor = texture2D(u_texture,v_textCoords) * v_color.a;
//
//    float y = v_textCoords.y;
//
//    float bottomAlpha = smoothstep(0.0, u_bottomFade, y);
//    float topAlpha = smoothstep(0.0, u_topFade, top - y);
//    float fadeAlpha = bottomAlpha * topAlpha;
//
//    textureColor.a = textureColor.a * fadeAlpha;
//
//    gl_FragColor = textureColor;
//
//
//}

#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_textCoords;

uniform sampler2D u_texture;

uniform float u_bottomFade;
uniform float u_topFade;
uniform float top;

uniform float u_curve;

float curveFade(float t) {
    t = clamp(t, 0.0, 1.0);

    t = smoothstep(0.0, 1.0, t);

    t = pow(t, u_curve);

    return t;
}

void main() {
    vec4 textureColor = texture2D(u_texture, v_textCoords) * v_color;

    float y = v_textCoords.y;

    float bottomT = y / u_bottomFade;
    float topT = (top - y) / u_topFade;

    float bottomAlpha = curveFade(bottomT);
    float topAlpha = curveFade(topT);

    float fadeAlpha = bottomAlpha * topAlpha;

    textureColor.a *= fadeAlpha;

    gl_FragColor = textureColor;
}