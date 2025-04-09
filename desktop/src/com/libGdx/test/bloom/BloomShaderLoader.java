package com.libGdx.test.bloom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class BloomShaderLoader {
    public static final ShaderProgram createShader(String str, String str2) {
        String readString = Gdx.files.classpath("bloomshaders/" + str + ".vertex.glsl").readString();
        String readString2 = Gdx.files.classpath("bloomshaders/" + str2 + ".fragment.glsl").readString();
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(readString, readString2);
        if (!shaderProgram.isCompiled()) {
            System.out.println(shaderProgram.getLog());
            Gdx.app.exit();
        }
        return shaderProgram;
    }
}
