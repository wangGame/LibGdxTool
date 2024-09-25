#ifdef GL_ES
precision mediump float;
#endif
//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float type;
uniform sampler2D u_texture2;
vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main() {
//     vec4 textureColor = v_color* texture2D(u_texture,v_textCoords);
//
//     if(type == 1.0){
//          float luminance = dot(textureColor.rgb, W);
//          float a = luminance;
//          finalColor = vec4(vec3(a),textureColor.a*0.6);
//     }else{
//          finalColor = textureColor;
//     }
     vec4 pic = v_color * texture2D(u_texture, v_textCoords);
     vec4 finalColor = pic;
     if(type < 1.5) {
          // 红色
          vec3 color1 = vec3(0.29803923, 0.2901961, 0.2509804);
          // 蓝色
          vec3 color2 = vec3(0.85882354, 0.85490197, 0.7921569);
          // 插值因子，取0.5表示取两者之间的中间色
          float t = (pic.r + pic.g + pic.b) / 3.0;
          //    float t = dot(pic.rgb, W);
          // 计算过渡颜色
          vec3 interpolatedColor = mix(color1, color2, t);
          finalColor=vec4(interpolatedColor.rgb,pic.a);
     }else{
          finalColor = pic;
     }
     vec4 xnv =  v_color * texture2D(u_texture2,v_textCoords);
     gl_FragColor = finalColor;
     gl_FragColor.a = xnv.a;
}
