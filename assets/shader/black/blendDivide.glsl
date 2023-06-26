
#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture1;
uniform float time;
float OFFSET = 0.4;

uniform float u;
uniform float u2;
uniform float v;
uniform float v2;

void main() {


    vec4 base = v_color* texture2D(u_texture, v_textCoords);
    vec4 overlay = v_color* texture2D(u_texture1, v_textCoords);


    time = time*4+7;
    overlay.a +=1.0;
    overlay.a -= time * 0.1f;
    if(overlay.a < 0.0){
        overlay.a = 0.0;
    }
    float ra;
    if (overlay.a == 0.0 || ((base.r / overlay.r)-time > (base.a / overlay.a)))
    ra = overlay.a * base.a + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);
    else
    //        discard;
    ra = (base.r * overlay.a * overlay.a) / overlay.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);


    float ga;
    if (overlay.a == 0.0 || ((base.g / overlay.g)-time > (base.a / overlay.a)))
    ga = overlay.a * base.a + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);
    else
    ga = (base.g * overlay.a * overlay.a) / overlay.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);


    float ba;
    if (overlay.a == 0.0 || ((base.b / overlay.b)-time > (base.a / overlay.a)))
    ba = overlay.a * base.a + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);
    else
    ba = (base.b * overlay.a * overlay.a) / overlay.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);

    float a = overlay.a + base.a - overlay.a * base.a;
    float xx = (ra + ga + ba)/3.0;

    if(xx > 1.0){
        float minus = (xx - 1.0);
        float enda = (0.4 - minus)*2.5;
        if(enda<0){
            enda = 0.0;
        }
        gl_FragColor = vec4(base.rgb, enda*base.a);
    } else{
        gl_FragColor = vec4(base.rgb, a*base.a);
    }



    //
    //    float vvv = (v_textCoords.y - v) / (v2 - v);
    //    float uuu = (v_textCoords.x - u) / (u2 - u);
    //    vec2 vecCoords = vec2(vvv,uuu);
    //    vec4 sourceColor = texture2D(u_texture, v_textCoords) * v_color;
    //    vec4 sourceColor2 = texture2D(u_texture1, vecCoords) * v_color;
    //    float xx = 1-(sourceColor2.r + sourceColor2.g + sourceColor2.b) / 3.0;
    //    xx = xx * xx * xx;
    //    xx = xx - 0.5 + time;
    //    gl_FragColor = vec4(sourceColor.rgb,sourceColor.a * xx);
    //







}
