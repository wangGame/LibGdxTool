#ifdef GL_ES

#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;


void main() {
//    part1
//    float scale = 0.07; // 增加缩放值以增强效果
//    float focus = 0.1;
//
//    float map = (texture(u_texture, v_textCoords) * v_color).r;
//    map = map * -1.0 + focus;
////    float offset = 0.3;
//    // 根据深度值计算新的纹理坐标，应用了偏移和缩放
////    vec2 disCords = v_textCoords + offset * map * scale;
////    vec4 o = texture2D(u_texture, disCords);
//
////    if(map<0.0){
////
////
////        gl_FragColor = new vec4(map,map,map,1.0);
////
////    }else{
//        gl_FragColor = new vec4(map,map,map,1.0);
////    }


//
//    float scale = 0.07; // 增加缩放值以增强效果
//    float focus = 0.1;
//
//    float map = (texture(u_texture, v_textCoords) * v_color).r;
//    map = map * -1.0 + focus;
//    gl_FragColor = new vec4(map,map,map,1.0);


    vec4 textColor = texture(u_texture, v_textCoords) * v_color;
    float depth = dot(textColor.rgb, vec3(0.299, 0.587, 0.114));
    gl_FragColor = new vec4(depth,depth,depth,1.0);

}