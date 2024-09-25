#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float exposure;
uniform float offset;
uniform float gamma;
void main() {

        vec4 textureColor = v_color* texture2D(u_texture,v_textCoords);
        //    vec3 endColor =max(min(textureColor.rgb * pow(2.0, exposure/(2.2f*gamma))+offset,1.0),0.0);
        float xx = (textureColor.r + textureColor.g + textureColor.b) / 3.0;
        vec3 endColor =max(min(textureColor.rgb * pow(2.0, (exposure)/(2.2*gamma))+offset,1.0),0.0);

        if(xx>214.0/255.0){
             endColor =max(min(textureColor.rgb * pow(2.0, (exposure* 0.5)/(2.2*gamma))+offset,1.0),0.0);
        }else
        if(xx > 100.0/255.0){
              float xxx =   (( xx * 214.0 - 100.0 ) / (214.0 - 100.0) * 0.5);
              endColor =max(min(textureColor.rgb * pow(2.0, (exposure* (1.0 -  xxx))/(2.2*gamma))+offset,1.0),0.0);
        }
        gl_FragColor = vec4(endColor, textureColor.w);
}