#ifdef GL_ES

#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;
uniform float wave_offset;
uniform vec2 center;

void main() {
//    vec4 textureColor = v_color * texture2D(u_texture,v_textCoords);
//    gl_FragColor = textureColor;

//    vec2 uv = normalize(vec2(0.5, 0.5) - v_textCoords) * 0.2 * sin(time) + v_textCoords;
//    vec4 textureColor = v_color * texture2D(u_texture,uv);
//    gl_FragColor = textureColor;



//    vec2 distance_vec = vec2(0.5, 0.5) - v_textCoords;
//    float sin_factor = sin(time) * 0.2;
//
//    float wave_radius = 0.3;
//    float distance = sqrt(distance_vec.x * distance_vec.x + distance_vec.y * distance_vec.y);
//    // 其中waveOffset是随时间增长的，通过外部传入
//    float dis_factor = clamp(wave_radius - abs(distance - 0.2), 0.0, 1.0);
//
//    vec2 uv = v_textCoords + normalize(distance_vec) * sin_factor * dis_factor;
//
//    vec4 textureColor = v_color * texture2D(u_texture,uv);
//    gl_FragColor = textureColor;



    float wave_radius = 0.9;

    vec2 distance_vec = center - v_textCoords;
    distance_vec = distance_vec * vec2(1.0, 1.0);
    float distance = sqrt(distance_vec.x * distance_vec.x + distance_vec.y * distance_vec.y);
    float sin_factor = sin(distance * 100.0 - time * 2) * 0.01;
    float discard_factor = clamp(wave_radius - abs(wave_offset - distance), 0.0, 1.0);

    vec2 offset = normalize(distance_vec) * sin_factor * discard_factor;
    vec2 uv = -offset + v_textCoords;

    gl_FragColor = texture2D(u_texture, uv);

}