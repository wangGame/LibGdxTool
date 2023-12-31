attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_controlPoint;

varying vec2 v_texCoord;

void main() {
    gl_Position = u_projTrans * a_position;
    v_texCoord = a_texCoord0;
}