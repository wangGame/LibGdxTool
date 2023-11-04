#ifdef GL_ES

#endif

//input from vertex shader
varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
//uniform float bigR1;
//uniform float bigR2;
//uniform float bigR3;

//uniform float smallR1;
//uniform float smallR2;
//uniform float smallR3;

void draw(float bigR,float smallR,float xxyy,vec4 textureColor){
    if(xxyy < bigR){
      if(xxyy > smallR){
           gl_FragColor = textureColor;
           gl_FragColor.a = (0.25 - bigR) * 4;
      }
    }
}

void main() {
    vec4 textureColor = v_color * texture2D(u_texture,v_textCoords);
    float xx = v_textCoords.x - 0.5;
    float yy = v_textCoords.y - 0.5;
    float xxyy = xx * xx + yy * yy;
    draw(0.25f,0.24f,xxyy,textureColor);
    draw(0.2f,0.19f,xxyy,textureColor);
    draw(0.15f,0.14f,xxyy,textureColor);
    draw(0.10f,0.09f,xxyy,textureColor);
    draw(0.05f,0.04f,xxyy,textureColor);
    draw(0.01f,0.009f,xxyy,textureColor);


  //  draw(bigR2,smallR2,xxyy,textureColor);
  //  draw(bigR3,smallR3,xxyy,textureColor);
}