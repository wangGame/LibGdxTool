#ifdef GL_ES

#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;

uniform float width;
uniform float time;

void main() {
    vec4 textColor = texture(u_texture, v_textCoords) * v_color;


    float stepTime = -width;
    stepTime += mod(time, 1.0 + 2.0 * width);

    if (v_textCoords.x >= -v_textCoords.y + stepTime && v_textCoords.x <= -v_textCoords.y + width + stepTime) {
        textColor *= 1.3;
    }

    gl_FragColor = textColor;
}