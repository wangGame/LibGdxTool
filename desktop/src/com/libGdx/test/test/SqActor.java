//package com.libGdx.test.test;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Mesh;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.PolygonRegion;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.graphics.g3d.ModelBatch;
//import com.badlogic.gdx.graphics.glutils.ShaderProgram;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.kw.gdx.asset.Asset;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//public class SqActor extends Actor {
//    private ShaderProgram program;
//    private Texture texture;
//
//
//    protected FloatBuffer vertexBuffer;
//    private  float[] triangleCoords;
//    private int glAPosition;
//    private int glUBaseColor;
//    private int glUMatrix;
//
//    public SqActor(){
//
//        ModelBatch modelBatch
//
//        String vertexShaderCode =
//                "attribute vec4 aPosition;\n" +
//                        "uniform mat4 uMatrix;\n" +
//                        "uniform vec4 uBaseColor;\n" +
//                        "varying vec4 vColor;\n" +
//                        "void main(){\n" +
//                        "    gl_Position=uMatrix*aPosition;\n" +
//                        "    vColor=uBaseColor;\n" +
//                        "}";
//
//        String fragmentShaderCode =
//                "precision mediump float;" +
//                        "varying vec4 vColor;" +
//                        "void main() {" +
//                        "  gl_FragColor = vColor;" +
//                        "}";
//
//        triangleCoords = new float[]{
//                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
//                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
//                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
//                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
//                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
//                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
//
//                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
//                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
//                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
//                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
//                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
//                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
//
//                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f,
//                -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f,
//                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f,
//                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f,
//                -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f,
//                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f,
//
//                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f,
//                0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
//                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
//                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
//                0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f,
//                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f,
//
//                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f,
//                0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f,
//                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f,
//                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f,
//                -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f,
//                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f,
//
//                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
//                0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
//                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f,
//                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f,
//                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f,
//                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f
//        };
//
//        float vv[] = new float[]{
//                0.0f,  0.0f,
//                1.0f,  0.0f,
//                1.0f,  1.0f,
//                1.0f,  1.0f,
//                0.0f,  1.0f,
//                0.0f,  0.0f,
//                0.0f,  0.0f,
//                1.0f,  0.0f,
//                1.0f,  1.0f,
//                1.0f,  1.0f,
//                0.0f,  1.0f,
//                0.0f,  0.0f,
//                1.0f,  0.0f,
//                1.0f,  1.0f,
//                0.0f,  1.0f,
//                0.0f,  1.0f,
//                0.0f,  0.0f,
//                1.0f,  0.0f,
//                1.0f,  0.0f,
//                1.0f,  1.0f,
//                0.0f,  1.0f,
//                0.0f,  1.0f,
//                0.0f,  0.0f,
//                1.0f,  0.0f,
//                0.0f,  1.0f,
//                1.0f,  1.0f,
//                1.0f,  0.0f,
//                1.0f,  0.0f,
//                0.0f,  0.0f,
//                0.0f,  1.0f,
//                0.0f,  1.0f,
//                1.0f,  1.0f,
//                1.0f,  0.0f,
//                1.0f,  0.0f,
//                0.0f,  0.0f,
//                0.0f,  1.0f
//        };
//
//        vertexBuffer = floatbuffer(triangleCoords);
//
//        texture = Asset.getAsset().getTexture("000.png");
//        program = new ShaderProgram(vertexShaderCode,
//                fragmentShaderCode);
//
//        GL20 gl = Gdx.gl20;
//        int programId = program.getProgram();
//        glAPosition = gl.glGetAttribLocation(programId, "aPosition");
//        glUMatrix = gl.glGetUniformLocation(programId, "uMatrix");
//        glUBaseColor = gl.glGetUniformLocation(programId, "uBaseColor");
//    }
//
//
//    public FloatBuffer floatbuffer(float[] triangleCoords){
//        ByteBuffer bb = ByteBuffer.allocateDirect(
//                triangleCoords.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        FloatBuffer vertexBuffer = bb.asFloatBuffer();
//        vertexBuffer.put(triangleCoords);
//        vertexBuffer.position(0);
//        return vertexBuffer;
//    }
//
//
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        GL20 gl = Gdx.gl20;
//
//        program.begin();
//        vertexBuffer.position(0);
//        gl.glUniform4f(glUBaseColor, 1.0f, 1.0f, 1.0f, 1.0f);
//
//        //传入顶点信息
//        gl.glEnableVertexAttribArray(glAPosition);
//        gl.glVertexAttribPointer(glAPosition, 3, gl.GL_FLOAT, false, 6 * 4, vertexBuffer);
//        gl.glDrawArrays(gl.GL_TRIANGLES, 0, triangleCoords.length / 6);
//        //禁止顶点数组的句柄
//        gl.glUniformMatrix4fv(glUMatrix, 1, false, batch.getProjectionMatrix().getValues(), 0);
//        program.end();
//    }
//}
