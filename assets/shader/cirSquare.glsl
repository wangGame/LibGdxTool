#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_textCoords;
uniform sampler2D u_texture;
uniform float uv;
uniform float vv;
uniform float ra;
uniform float h;
uniform float rato;

void main() {
    // Input info
    vec2 boxPos; // The position of the center of the box (in normalized coordinates)
    vec2 boxBnd; // The half-bounds (radii) of the box (in normalzied coordinates)
    float radius;// Radius

    //center
    boxPos = vec2(uv, vv);	// center of the screen
    //with height
    boxBnd = vec2(uv-ra, vv-ra-h);  // half of the area
    radius = ra;

    // Normalize the pixel coordinates (this is "passTexCoords" in your case)
    vec2 uv = v_textCoords;

    vec2 aspectRatio = vec2(rato,1.0);

    // In order to make sure visual distances are preserved, we multiply everything by aspectRatio
    uv *= aspectRatio;
    boxPos *= aspectRatio;
    boxBnd *= aspectRatio;

    vec4 tc = v_color * texture2D(u_texture, v_textCoords);
    //    gl_FragColor = mix(quadColor, tc, shadowAlpha - smoothedAlpha);
    //    gl_FragColor = tc;
    //    gl_FragColor.a = shadowAlpha - smoothedAlpha;
    // Time varying pixel color
    vec3 col = tc.rgb;

    // Output to screen
    float alpha = length(max(abs(uv - boxPos) - boxBnd, 0.0)) - radius;

    // Shadertoy doesn't have an alpha in this case
    if(alpha <= 0.0){
        gl_FragColor = vec4(col,tc.a);
    }else{
        gl_FragColor = vec4(1.0, 1.0, 1.0, 0.0);
    }
}