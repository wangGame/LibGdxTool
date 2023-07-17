#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform vec2 u_controlPoint;

varying vec2 v_texCoord;

void main() {
    // 计算纹理坐标
    float t = gl_FragCoord.x / u_resolution.x;
    float x = 0;
    float y = gl_FragCoord.y;

    vec2 texCoord = vec2(x / u_resolution.x, y / u_resolution.y);

    gl_FragColor = texture2D(u_texture, texCoord);
}