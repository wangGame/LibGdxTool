package com.kw.gdx.utils.hui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HuiGroup extends Group {
    private static ShaderProgram shadowProgram;

    public static ShaderProgram createShadowPropgram() {
        String vert = "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "varying vec4 v_color;\n" +
                "uniform mat4 u_projTrans;\n" +
                "varying vec2 v_textCoords;\n" +
                "\n" +
                "void main() {\n" +
                "\tv_color = a_color;\n" +
                "\tv_color.a = v_color.a * (255.0/254.0);\n" +
                "\tv_textCoords = a_texCoord0;\n" +
                "\tgl_Position = u_projTrans * a_position;\n" +
                "}";
        String frag = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "\n" +
                "\n" +
                "//input from vertex shader\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_textCoords;\n" +
                "uniform sampler2D u_texture;\n" +
                "\n" +
                "\n" +
                "void main() {\n" +
                "//    vec4 tempColor = v_color* texture2D(u_texture,v_textCoords);\n" +
                "//       if(tempColor.a>0.1){\n" +
                "//           gl_FragColor = vec4(0.3, 0.3, 0.3, tempColor.a);\n" +
                "//       }else{\n" +
                "//           gl_FragColor = vec4(1, 1, 1, tempColor.a);\n" +
                " //      }\n" +
                "    vec4 tempColor = v_color* texture2D(u_texture,v_textCoords);\n" +
                "    float c=(tempColor.r + tempColor.g + tempColor.b)/3.0;\n" +
                "    gl_FragColor = vec4(c, c, c, tempColor.a);\n" +
                "\n" +
                "\n" +
                "\n" +
                "}";
        if (shadowProgram == null) {
            shadowProgram = new ShaderProgram(vert, frag);
        }
        return shadowProgram;
    }

    public HuiGroup(Image image){
        createShadowPropgram();
        addActor(image);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!unuse){
            super.draw(batch, parentAlpha);
        }else {
            if (shadowProgram != null) {
                batch.setShader(shadowProgram);
            }
            super.draw(batch, parentAlpha);
            if (batch != null) {
                batch.setShader(null);
            }
        }
    }
    private boolean unuse = false;

    public void setUnuse(boolean unuse) {
        this.unuse = unuse;
    }
}
