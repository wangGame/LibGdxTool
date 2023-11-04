
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;

vec3 palette(float t) {
    vec3 a = vec3(0.938, 0.328, 0.718);
    vec3 b = vec3(0.659, 0.438, 0.328);
    vec3 c = vec3(0.388, 0.388, 0.296);
    vec3 d = vec3(2.538, 2.478, 0.168);
    return a + b*cos(6.28*(c * t * d));
}

void main() {
    vec4 textureColor = v_color * texture2D(u_texture,v_textCoords);
    vec2 uv = v_textCoords;
    uv.x = 0.5 - uv.x;
    uv.y = 0.5 - uv.y;
    float d = length(uv);
    vec3 col = palette(d + time);
    d = sin(d * 30.0 +time) / 2.0;
    d = abs(d);
    d = 0.01 / d;
    col *= d;
    gl_FragColor = vec4(col, 1.0);
}


