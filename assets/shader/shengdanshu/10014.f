#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float hash(vec2 p){
    return fract(sin(dot(p, vec2(127.1,311.7))) * 43758.5453);
}

float noise(vec2 p){
    vec2 i = floor(p);
    vec2 f = fract(p);

    float a = hash(i);
    float b = hash(i + vec2(1.0,0.0));
    float c = hash(i + vec2(0.0,1.0));
    float d = hash(i + vec2(1.0,1.0));

    vec2 u = f*f*(3.0-2.0*f);

    return mix(a,b,u.x) + (c-a)*u.y*(1.0-u.x) + (d-b)*u.x*u.y;
}

void main(){

    vec2 uv = gl_FragCoord.xy / u_resolution;

    vec2 p = (uv - 0.5) * 4.0;
    p.x *= u_resolution.x / u_resolution.y;

    // 🌪 DOMAIN WARP（关键）
    vec2 warp = vec2(
        noise(p + u_time * 0.2),
        noise(p + 10.0 + u_time * 0.2)
    );

    p += (warp - 0.5) * 1.5;

    // 🎯 被扭曲的函数
    float y = sin(p.x * 3.0);

    float dist = abs(p.y - y);

    float w = fwidth(p.y);

    float line = smoothstep(w*1.5, 0.0, dist);

    vec3 color = vec3(0.4, 0.7, 1.5) * line;

    gl_FragColor = vec4(color, 1.0);
}