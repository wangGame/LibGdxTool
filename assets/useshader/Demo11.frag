#ifdef GL_ES
precision mediump float;
#endif
//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;

uniform float u;
uniform float u2;
uniform float v;
uniform float v2;

#define NOISE_TYPE_PERLIN 1
#define NOISE_TYPE_VALUE 2
#define NOISE_TYPE NOISE_TYPE_PERLIN

#define MOD3 vec3(0.1071,0.11369,0.13787)
//#define MOD3 vec3(443.8975,397.2973, 491.1871)
float hash31(vec3 p3)
{
    p3  = fract(p3 * MOD3);
    p3 += dot(p3, p3.yzx + 19.19);
    return -1.0 + 2.0 * fract((p3.x + p3.y) * p3.z);
}

vec3 hash33(vec3 p3)
{
    p3 = fract(p3 * MOD3);
    p3 += dot(p3, p3.yxz+19.19);
    return -1.0 + 2.0 * fract(vec3((p3.x + p3.y)*p3.z, (p3.x+p3.z)*p3.y, (p3.y+p3.z)*p3.x));
}

// ========= Noise ===========
#if NOISE_TYPE == NOISE_TYPE_VALUE
float value_noise(vec3 p)
{
    vec3 pi = floor(p);
    vec3 pf = p - pi;

    vec3 w = pf * pf * (3.0 - 2.0 * pf);

    return 	mix(
        mix(
            mix(hash31(pi + vec3(0.0, 0.0, 0.0)), hash31(pi + vec3(1.0, 0.0, 0.0)), w.x),
            mix(hash31(pi + vec3(0.0, 0.0, 1.0)), hash31(pi + vec3(1.0, 0.0, 1.0)), w.x),
            w.z),
        mix(
            mix(hash31(pi + vec3(0, 1, 0)), hash31(pi + vec3(1, 1, 0)), w.x),
            mix(hash31(pi + vec3(0, 1, 1)), hash31(pi + vec3(1, 1, 1)), w.x),
            w.z),
        w.y);
}
#define noise(p) value_noise(p)
#else
float perlin_noise(vec3 p)
{
    vec3 pi = floor(p);
    vec3 pf = p - pi;

    vec3 w = pf * pf * (3.0 - 2.0 * pf);

    return 	mix(
        mix(
            mix(dot(pf - vec3(0, 0, 0), hash33(pi + vec3(0, 0, 0))),
                dot(pf - vec3(1, 0, 0), hash33(pi + vec3(1, 0, 0))),
                w.x),
            mix(dot(pf - vec3(0, 0, 1), hash33(pi + vec3(0, 0, 1))),
                dot(pf - vec3(1, 0, 1), hash33(pi + vec3(1, 0, 1))),
                w.x),
            w.z),
        mix(
            mix(dot(pf - vec3(0, 1, 0), hash33(pi + vec3(0, 1, 0))),
                dot(pf - vec3(1, 1, 0), hash33(pi + vec3(1, 1, 0))),
                w.x),
            mix(dot(pf - vec3(0, 1, 1), hash33(pi + vec3(0, 1, 1))),
                dot(pf - vec3(1, 1, 1), hash33(pi + vec3(1, 1, 1))),
                w.x),
            w.z),
        w.y);
}
#define noise(p) perlin_noise(p)
#endif

float fbm0(vec2 p)
{
    float d = 0.0;
    //const mat2 m = mat2( 1.6,  1.2, -1.2,  1.6 );
    float m = 4.0;
    d += 0.5 * noise(p.xyy); p = m * p;
    d += 0.25 * noise(p.xyy); p = m * p;
    d += 0.125 * noise(p.xyy); p = m * p;
    d += 0.0625 * noise(p.xyy); p = m * p;
    return d;
}

float fbm1(vec2 p)
{
    float d = 0.0;
    //const mat2 m = mat2( 1.6,  1.2, -1.2,  1.6 );
    float m = 1.5;
    d -= 0.5 * noise(p.xyy); p = m * p;
    d -= 0.25 * noise(p.xyy); p = m * p;
    d -= 0.125 * noise(p.xyy); p = m * p;
    d -= 0.0625 * noise(p.xyy); p = m * p;
    return d;
}

float FBM(vec2 p)
{
    float d = 0.0;
    p.xy -= 0.5;
    vec2 op = p;
    p *= 4.0;
    if (p.x < 10.0) {
        float w = 1.0;
        d += w * noise(p.xyy);

        p *= 4.0;
        w /= 8.0;
        //float m = 1.8;
        const mat2 m = mat2( 1.6,  1.2, -1.2,  1.6 );
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
    }
    else {
        if (p.x < 0.0) {
            //return fbm0((p+vec2(0.5,0.0))*10.);
            d = noise((p+vec2(0.5,0.0)).xyy*10.);
        }
        else {
            d = fbm1(p*2.);
        }
        d = fbm1(p*1.);
    }

    //float l = length(op) - 0.5*sqrt(2.0);
    //d += l;

    d += 0.5;

    float x = clamp(length(op) / (0.5*sqrt(2.0)), 0.0, 1.0);
    x = pow(x,1.0);
    d *= x;

    d -= 0.5;

    return max(d, -0.5);
}

float sdGrid(vec2 coord, float num)
{
    vec2 f = fract(coord.xy * num);
    return 1.0 - smoothstep(0.00,0.01,min(f.x,f.y));
}

#define COLOR_LEAD vec3(1,1, 1)
#define COLOR_TRAIL vec3(1,1, 1)
#define COLOR_BLACK vec3(1, 1, 1)





void main() {

    vec4 c = v_color* texture2D(u_texture,v_textCoords);
    float u = v_textCoords.x/abs(u2 - u);
    float v = v_textCoords.y/abs(v2 - v);
    vec2 uv = vec2(u,v);
    //vec3 heightmap = texture(iChannel2, uv).rrr;
    vec3 heightmap  = vec3(FBM(uv)+0.5);
//    vec3 background = vec4(1,1,1,0).rgb;
    vec3 background = vec3(0,0,0);
    vec3 foreground = vec3(1,1,1);

    float t = fract(-time*0.2);
    //vec3 erosion = smoothstep(t-0.1*length(uv-0.5)*4., t, heightmap);
    vec3 erosion = smoothstep(t-0.2, t, heightmap);



    vec3 border = smoothstep(0.0, 0.1, erosion) - smoothstep(0.1, 1.0, erosion);
    float xxxx = (border.r + border.g + border.b)/3.0;

    vec3 bordercolor = mix(COLOR_LEAD, COLOR_TRAIL, smoothstep(0.1, 1.0, border));
    vec3 col = mix(foreground, bordercolor, step(0.01, border));
    col = mix(col, background, erosion);
//col = heightmap;
//    vec4 vv = vec4(1,1,1,xxxx);
//    vv
    float xx = (col.r + col.g + col.b) / 3.0;
    c.a = c.a *(1.0-xx);
    gl_FragColor = c ;// + vec4(sdGrid(uv-0.5, 2.));
}