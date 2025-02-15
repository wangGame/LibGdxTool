#ifdef GL_ES
precision highp float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_ballMatrix;

uniform vec3 u_ballPos;
uniform vec3 u_lightPos;
uniform vec3 u_viewPos;

uniform samplerCube u_environmentCubemap;

void main()
{

    vec2 v_ballPosition = v_texCoords*2.0-1.0;
    float r = dot(v_ballPosition.xy, v_ballPosition.xy);

    vec4 realP = vec4(v_ballPosition.x, v_ballPosition.y, sqrt(1.0 - r), 1.0);

    //法向量
    vec3 norm =normalize(realP.xyz);
    //光的方向
    vec3 lightDir = normalize(u_lightPos - (u_ballPos+realP.xyz));

    //点乘夹角 得到漫反射光
    float diff = max(dot(norm, lightDir), 0.0)*0.6;
    //        vec3 diffuse = light.diffuse *diff * vec3(texture(material.diffuse, TexCoords));


    float lightv =diff+0.4;
    //        lightv=1.0-(1.0-lightv)*3.0/5.0;
    vec4 realP2 = u_ballMatrix * realP;
    // 计算球面贴图
    float ty=acos(-realP2.y) / 3.1415926535897932384626433832795;
    if(realP2.y>0.0){
        ty=1.0-acos(realP2.y) / 3.1415926535897932384626433832795;
    }
    vec2 texcoord = vec2(
    acos(clamp(realP2.z/ sqrt(max(1.0 - realP2.y * realP2.y, 0.0)), -1.0, 1.0)) / 6.283185307179586476925286766559,
    ty);
    texcoord.x = texcoord.x * (1.0 - step(realP2.x, 0.0)*2.0) + 0.5;
    vec4 retColor = texture2D(u_texture, texcoord);
    retColor.a=1.0-(r-0.95)/0.05;

    //    镜面光
    vec3 viewDir = normalize(u_viewPos - (u_ballPos+realP.xyz));

    vec3 reflectDir =normalize(reflect(-viewDir, norm));

    float hx= reflectDir.x;
    float hy= reflectDir.z;
    float hz= reflectDir.y;

    reflectDir.x=hx;
    reflectDir.y=hy;
    reflectDir.z=hz;

    vec4 highlight=textureCube(u_environmentCubemap, reflectDir)*0.4;
    highlight.a=0.0;

    //        vec4 li=vec4(0.9882,0.9803,0.8941,1.0);
    vec4 li=vec4(0.99, 0.98, 0.88, 1.0);

    gl_FragColor =li* v_color * vec4(lightv * retColor.xyz, retColor.w)+vec4(highlight.rgb, 0.0);
    //        gl_FragColor = highlight.rgba;


}
