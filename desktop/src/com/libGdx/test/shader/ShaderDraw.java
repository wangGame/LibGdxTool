package com.libGdx.test.shader;

import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderDraw extends ImmediateModeRenderer20 {
    public ShaderDraw(boolean hasNormals, boolean hasColors, int numTexCoords) {
        super(hasNormals, hasColors, numTexCoords);
    }

    public ShaderDraw(int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords) {
        super(maxVertices, hasNormals, hasColors, numTexCoords);
    }

    public ShaderDraw(int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords, ShaderProgram shader) {
        super(maxVertices, hasNormals, hasColors, numTexCoords, shader);
    }
}
