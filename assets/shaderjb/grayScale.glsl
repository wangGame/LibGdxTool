#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_textCoords;

uniform sampler2D u_texture;
uniform float u_bottomFade;
uniform float u_topFade;
uniform float top;

void main() {
    vec4 textureColor = texture2D(u_texture,v_textCoords) * v_color.a;

    float y = v_textCoords.y;

    float bottomAlpha = smoothstep(0.0, u_bottomFade, y);
    float topAlpha = smoothstep(0.0, u_topFade, top - y);
    float fadeAlpha = bottomAlpha * topAlpha;

    textureColor.a = textureColor.a * fadeAlpha;

    gl_FragColor = textureColor;


}