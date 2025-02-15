//package com.libGdx.test.test;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.glutils.ShaderProgram;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//
//public class CV extends Actor {
//    private int glProgramId;
//    protected int glUMatrix;
//    protected int glULightPosition;
//    protected int glAPosition;
//    protected int glANormal;
//    protected int glUAmbientStrength;
//    protected int glUDiffuseStrength;
//    protected int glUSpecularStrength;
//    protected int glUBaseColor;
//    protected int glULightColor;
//
//    protected int glHTexture;
//    protected int glHCoordinate;
//    float lx = 0f;
//    float ly = 0.8f;
//    float lz = -1f;
//
//    public CV() {
//
//
//
//        program = new ShaderProgram(Gdx.,fragmentShaderCode);
//    }
//
//    private ShaderProgram program;
//
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//
//        batch.setShader(program);
//
//        vertexBuffer.position(0);
//        //准备三角形的坐标数据
//        //环境光强度
//        GLES20.glEnableVertexAttribArray(glHCoordinate);
//        GLES20.glUniform1f(glUAmbientStrength, 0.5F);
//        //漫反射光强度
//        GLES20.glUniform1f(glUDiffuseStrength, 0.3F);
//        //镜面光强度
//        GLES20.glUniform1f(glUSpecularStrength, 0.8F);
//        //光源颜色
//        GLES20.glUniform3f(glULightColor, 1.0f, 1.0f, 1.0f);
//        //物体颜色
//        GLES20.glUniform4f(glUBaseColor, 0.0f, 1.0f, 1.0f, 1.0f);
//        //光源位置
//        GLES20.glUniform3f(glULightPosition, lx, ly, lz);
//        //传入顶点信息
//        GLES20.glEnableVertexAttribArray(glAPosition);
//        GLES20.glVertexAttribPointer(glAPosition, 3, GLES20.GL_FLOAT, false, 8 * 4, vertexBuffer);
//        //传入法线信息
//        GLES20.glEnableVertexAttribArray(glANormal);
//        vertexBuffer.position(3);
//        GLES20.glVertexAttribPointer(glANormal, 3, GLES20.GL_FLOAT, false, 8 * 4, vertexBuffer);
//        vertexBuffer.position(6);
//        GLES20.glVertexAttribPointer(glHCoordinate, 2, GLES20.GL_FLOAT, false, 8 * 4, vertexBuffer);
//        GLES20.glUniformMatrix4fv(glUMatrix, 1, false, utils.getmMVPMatrix(), 0);
//
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, triangleCoords.length / 6);
//        //禁止顶点数组的句柄
//
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
//        GLES20.glDisableVertexAttribArray(glAPosition);
//        GLES20.glDisableVertexAttribArray(glANormal);
//
//    }
//}
//
//}
