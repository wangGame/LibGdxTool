#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;


void main() {

     float radius = 0.04;
     vec4 accum = vec4(0.0);
     vec4 normal = vec4(0.0);
     normal = texture2D(u_texture, vec2(v_textCoords.x, v_textCoords.y));
     accum += texture2D(u_texture, vec2(v_textCoords.x - radius, v_textCoords.y - radius));
     accum += texture2D(u_texture, vec2(v_textCoords.x + radius, v_textCoords.y - radius));
     accum += texture2D(u_texture, vec2(v_textCoords.x + radius, v_textCoords.y + radius));
     accum += texture2D(u_texture, vec2(v_textCoords.x - radius, v_textCoords.y + radius));
     accum.rgb =  vec3(1,1,0) * accum.a;
     accum.a = 1.0;
     //绘制的范围从  原图哪里来，  自己没有权力绝对   留还是不留
     normal = ( accum * (1.0 - normal.a)) + (normal * normal.a);
     gl_FragColor = v_color * normal;


  //  vec4 tempColor = v_color* texture2D(u_texture,v_textCoords);
   // float c=(tempColor.r + tempColor.g + tempColor.b)/3.0;
   // gl_FragColor = vec4(c, c, c, tempColor.a);



}