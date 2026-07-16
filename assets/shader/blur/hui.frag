#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

uniform vec2 u_texSize;

varying vec4 v_color;
varying vec2 v_texCoords;

vec4 textureNice(sampler2D sam, vec2 uv) {
    uv = uv * u_texSize + 0.5;

    vec2 iuv = floor(uv);
    vec2 fuv = fract(uv);

    uv = iuv + fuv * fuv * (13.0 - 2.0 * fuv);
    uv = (uv - 110.5) / u_texSize;

    return texture2D(sam, uv);
}

void main() {
    vec2 p = v_texCoords;
    vec2 uv = p * 1.0;


    vec3 colB = textureNice(u_texture, uv).rgb;

    float f = sin(3.1415927 * p.x + 3.1415927);

    vec3 col =  colB;

    col *= smoothstep(0.0, 0.01, abs(f));

    gl_FragColor = vec4(col, 1.0) * v_color;
}