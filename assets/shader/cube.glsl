#version 300 es

in vec3 a_position;
in vec4 a_color;

uniform mat4 u_mvpTrans; // MVP矩阵变换

out vec4 v_color;

void main() {
    gl_Position = u_mvpTrans * vec4(a_position, 1.0);
    v_color = a_color;
}