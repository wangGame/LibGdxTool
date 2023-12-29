attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

uniform mat4 u_projTrans;
uniform float u_time;

varying vec2 v_texCoord0;
varying vec4 v_color;

void main() {
    gl_Position = u_projTrans * a_position;
    v_texCoord0 = a_texCoord0;
    v_color = a_color;
}