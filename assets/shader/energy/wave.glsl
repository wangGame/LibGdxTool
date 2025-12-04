#ifdef GL_ES

#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;

uniform vec2 u_point;

float energy(float r, vec2 point1, vec2 point2) {
    return (r * r) / ((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
}

void main() {


    vec4 textColor = texture(u_texture, v_textCoords) * v_color;
    float u_radius = 0.05;
    float fragEnergy = energy(u_radius + 0.1, v_textCoords.xy, vec2(0.5)) + energy(u_radius, v_textCoords.xy, u_point);
    textColor.a = smoothstep(0.95, 1.0, fragEnergy);
    gl_FragColor = textColor;

}