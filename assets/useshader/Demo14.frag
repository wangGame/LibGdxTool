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
    vec2 bottomTextureCoordinate = v_textCoords;
    bottomTextureCoordinate.y += offset;
    vec4 bottomColor = v_color*texture2D(u_texture, bottomTextureCoordinate);
    vec2 bottomLeftTextureCoordinate = v_textCoords;
    bottomLeftTextureCoordinate.x -= offset;
    bottomLeftTextureCoordinate.y += offset;
    vec4 bottomLeftColor =v_color* texture2D(u_texture, bottomLeftTextureCoordinate);

    vec2 bottomRightTextureCoordinate = v_textCoords;
    bottomRightTextureCoordinate.x += offset;
    bottomRightTextureCoordinate.y += offset;
    vec4 bottomRightColor = v_color*texture2D(u_texture, bottomRightTextureCoordinate);


    vec2 textureCoordinate = v_textCoords;
    vec4 centerColor = v_color*texture2D(u_texture, textureCoordinate);

    vec2 leftTextureCoordinate = v_textCoords;
    leftTextureCoordinate.x -= offset;
    vec4 leftColor = v_color*texture2D(u_texture, leftTextureCoordinate);


    vec2 rightTextureCoordinate = v_textCoords;
    rightTextureCoordinate.x += offset;
    vec4 rightColor = v_color*texture2D(u_texture, rightTextureCoordinate);

    vec2 topTextureCoordinate = v_textCoords;
    topTextureCoordinate.y -= offset;
    vec4 topColor = v_color*texture2D(u_texture, topTextureCoordinate);


    vec2 topRightTextureCoordinate = v_textCoords;
    topRightTextureCoordinate.x += offset;
    topRightTextureCoordinate.y -= offset;
    vec4 topRightColor =v_color * texture2D(u_texture, topRightTextureCoordinate);


    vec2 topLeftTextureCoordinate = v_textCoords;
    topLeftTextureCoordinate.x -= offset;
    topLeftTextureCoordinate.y -= offset;
    vec4 topLeftColor = v_color*texture2D(u_texture, topLeftTextureCoordinate);

    float hh = -topLeftColor.r - 2.0 * topColor.r - topRightColor.r
    + bottomLeftColor.r + 2.0 * bottomColor.r + bottomRightColor.r;
    float vv = -bottomLeftColor.r - 2.0 * leftColor.r -
    topLeftColor.r + bottomRightColor.r + 2.0 * rightColor.r+ topRightColor.r;
    //    float mag = length(vec2(h, v));
    float mag = length(vec2(hh, vv));
    mag = step(0.3, mag);

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
    gl_FragColor = finalV * v_color.a ;


    
}