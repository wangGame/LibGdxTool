#ifdef GL_ES

#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;

uniform float wh_ratio;


void main() {
    vec4 textColor = texture(u_texture, v_textCoords) * v_color;
//    textColor.a = step(length(v_textCoords - vec2(0.5,0.5)), 0.5);
//    gl_FragColor = textColor;


// one
//    vec2 center = vec2(0.5f,0.5f);
//
//    float rx = center.x * wh_ratio;
//    float ry = center.y;
//    float dis = (v_textCoords.x * wh_ratio - rx) * (v_textCoords.x * wh_ratio - rx) + (v_textCoords.y  - ry) * (v_textCoords.y - ry);
//
////    textColor.a = step(dis, 0.25);
//    textColor.a = smoothstep()
//    gl_FragColor = textColor;



// two
    float radius = 0.5;
    vec2 center = vec2(0.5f,0.5f);
    float circle = radius * radius;
    float rx = center.x * wh_ratio;
    float ry = center.y;
    float dis = (v_textCoords.x * wh_ratio - rx) * (v_textCoords.x * wh_ratio - rx) + (v_textCoords.y  - ry) * (v_textCoords.y - ry);

    textColor.a = smoothstep(circle,circle-0.001 ,dis);
    gl_FragColor = textColor;

}