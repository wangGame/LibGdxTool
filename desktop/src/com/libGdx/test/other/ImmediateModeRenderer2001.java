package com.libGdx.test.other;

import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/28 15:19
 */
public class ImmediateModeRenderer2001 extends ImmediateModeRenderer20 {
    public ImmediateModeRenderer2001(boolean hasNormals, boolean hasColors, int numTexCoords) {
        super(hasNormals, hasColors, numTexCoords);
    }

    public ImmediateModeRenderer2001(int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords) {
        super(maxVertices, hasNormals, hasColors, numTexCoords);
    }

    public ImmediateModeRenderer2001(int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords, ShaderProgram shader) {
        super(maxVertices, hasNormals, hasColors, numTexCoords, shader);
    }
}
