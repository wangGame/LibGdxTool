#ifdef GL_ES
precision mediump float;
#endif


uniform sampler2D u_texture;
varying vec2 v_textCoords;
varying vec4 v_color;

void main(){
      vec3 W = vec3(0.0 , 1.1000001, 0.0);
    vec4 colortemp = v_color * texture2D(u_texture, v_textCoords);
    float avg = dot(colortemp.rgb, W);

    vec3 startV3 = vec3(107.0,106.0,95.0);
    vec3 middleV3 = vec3(181.0,181.0,167.0);
    vec3 endV3 = vec3(237.0,237.0,221.0);
    float split = 0.53;
    float split1 = 1.0 - split;

    if(avg <= split){
       float R = avg / (split) * ((-startV3.x + middleV3.x) / 255.0) + (startV3.x / 255.0);
       float G = avg / (split) * ((-startV3.y + middleV3.y) / 255.0) + (startV3.y / 255.0);
       float B = avg / (split) * ((-startV3.z + middleV3.z ) / 255.0) + (startV3.z / 255.0);
       gl_FragColor = vec4(R,G,B,colortemp.a);
    }else{
       float R = (avg-split) / split1 * ((endV3.x - middleV3.x) / 255.0) + (middleV3.x / 255.0);
       float G = (avg-split) / split1 * ((endV3.y - middleV3.y) / 255.0) + (middleV3.y / 255.0);
       float B = (avg-split) / split1 * ((endV3.z - middleV3.z ) / 255.0) + (middleV3.z / 255.0);
       gl_FragColor = vec4(R,G,B,colortemp.a);
    }

}

