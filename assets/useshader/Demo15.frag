#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float time;
uniform sampler2D u_texture2;
uniform float u;
uniform float u2;
uniform float v;
uniform float v2;

void main() {
    float offset = 0.0005;
    vec4 finalV = v_color * texture2D(u_texture, v_textCoords);

    vec2 uv = v_textCoords;
    vec2 iResolution = vec2(382.0,510.0);

    vec4 pixel00 = texture(u_texture, uv.xy + (vec2(0, 0)/ iResolution.xy));
    vec4 pixeln1n1 = texture(u_texture, uv.xy + (vec2(-1, -1)/ iResolution.xy));
    vec4 pixeln10 = texture(u_texture, uv.xy + (vec2(-1, 0)/ iResolution.xy));
    vec4 pixeln11 = texture(u_texture, uv.xy + (vec2(-1, 1)/ iResolution.xy));
    vec4 pixel0n1 = texture(u_texture, uv.xy + (vec2(0, -1)/ iResolution.xy));
    vec4 pixel01 = texture(u_texture, uv.xy + (vec2(0, 1)/ iResolution.xy));
    vec4 pixel1n1 = texture(u_texture, uv.xy + (vec2(1, -1)/ iResolution.xy));
    vec4 pixel10 = texture(u_texture, uv.xy + (vec2(1, 0)/ iResolution.xy));
    vec4 pixel11 = texture(u_texture, uv.xy + (vec2(1, 1)/ iResolution.xy));
    // (-1,-1)  (-1,0)  (-1,1)
    // (0, -1)  (0, 0)  (0, 1)
    // (1, -1)  (1, 0)  (1, 1)
    vec3 Gx =  (((pixel00.xyz * vec3(0)) + (pixel01.xyz * vec3(-2)) + (pixel0n1.xyz * vec3(2)) + (pixel11.xyz * vec3(-1)) + (pixel10.xyz * vec3(0))
    + (pixel1n1.xyz * vec3(1)) + (pixeln10.xyz * vec3(0)) + (pixeln11.xyz * vec3(-1)) + (pixeln1n1.xyz * vec3(1))));
    vec3 Gy =  (((pixel00.xyz * vec3(0)) + (pixel01.xyz * vec3(0)) + (pixel0n1.xyz * vec3(0)) + (pixel11.xyz * vec3(-1)) + (pixel10.xyz * vec3(-2))
    + (pixel1n1.xyz * vec3(-1)) + (pixeln10.xyz * vec3(2)) + (pixeln11.xyz * vec3(1)) + (pixeln1n1.xyz * vec3(1))));
    vec3 Gx2 = vec3(pow(Gx.x, 3.0), pow(Gx.y, 3.0),pow(Gx.z, 3.0));
    vec3 Gy2 = vec3(pow(Gy.x, 3.0), pow(Gy.y, 3.0),pow(Gy.z, 3.0));
    vec3 tot = Gx2 + Gy2;
    vec3 G = vec3(sqrt(tot.x), sqrt(tot.y), sqrt(tot.z));
/*vec3 sum = ((pixel00 + pixeln1n1 + pixeln10 + pixeln11 + pixel0n1
                + pixel01 + pixel1n1 + pixel10 + pixel11).xyz) / vec3(9);*/ //Box Blur
/* vec3 sum = (((pixel00.xyz * vec3(8)) + (pixel01.xyz * vec3(-1)) + (pixel0n1.xyz * vec3(-1)) + (pixel11.xyz * vec3(-1)) + (pixel10.xyz * vec3(-1))
                + (pixel1n1.xyz * vec3(-1)) + (pixeln10.xyz * vec3(-1)) + (pixeln11.xyz * vec3(-1)) + (pixeln1n1.xyz * vec3(-1))));*/
    float mag = (G.r+G.g+G.b) / 3.0;


    vec4 tempColor = v_color*texture2D(u_texture, v_textCoords);

    float u = (v_textCoords.x - u)/abs(u2 - u);
    float v = (v_textCoords.y - v)/abs(v2 - v);
    vec2 tempCoords = vec2(u,v);
    vec4 tempColor2 =v_color * texture2D(u_texture2, tempCoords);
    float ss = (tempColor2.r + tempColor2.g + tempColor2.b) / 3.0;
    vec4 xcc = vec4(1.0, 1.0, 1.0, ss*0.1);
    vec4 cccc = xcc;
    cccc.a += 2.0*abs(time);
    float endA = tempColor.a * cccc.a;
    endA = clamp(endA,0.0,1.0);
    float endAddColor = 0.2-time*0.6;




    if(time>=0.6 && endAddColor <= 0.0){
        float current = 0.7 - time;
        if(current<=0.0){
            current = 0.0;
        }
        vec3 minusV = vec3(current);
        tempColor.rgb =tempColor.rgb + minusV;
        gl_FragColor = tempColor * v_color.a;
    }else{
        float current = 0.7 - time;
        if(current<=0.0){
            current = 0.0;
        }
        vec3 minusV = vec3(current);
        if(endAddColor<=0.0){
            endAddColor = 0.0;
        }
        if(mag>0.01){
            gl_FragColor = vec4(clamp(tempColor.rgb+vec3(endAddColor),0.0,1.0) + minusV,endA * v_color.a);
        }else{
            finalV.rgb = finalV.rgb + minusV;
            gl_FragColor = finalV * v_color.a ;
        }
    }
}