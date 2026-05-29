package com.libGdx.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ThreeStage {

    private Camera mCamera;

    private ShaderProgram mShaderProgram;

    private Mesh mMesh;

    private final Matrix4 mModelMatrix = new Matrix4();

    private float rx;
    private float ry;

    public ThreeStage() {

        initCamera();

        initShader();

        initMesh();
    }

    private void initCamera() {

        PerspectiveCamera camera =
                new PerspectiveCamera(
                        60,
                        Gdx.graphics.getWidth()*6,
                        Gdx.graphics.getHeight()*6
                );

        camera.near = 0.1f;

        camera.far = 2000f;

        camera.position.set(0, 0, 1980);

        camera.lookAt(0, 0, 0);

        camera.update();

        mCamera = camera;
    }

    private void initShader() {

        String vertex =
                Gdx.files.internal(
                        "shader/cube.glsl"
                ).readString();

        String fragment =
                Gdx.files.internal(
                        "shader/cubef.glsl"
                ).readString();

        mShaderProgram =
                new ShaderProgram(vertex, fragment);

        if (!mShaderProgram.isCompiled()) {

            throw new RuntimeException(
                    mShaderProgram.getLog()
            );
        }
    }

    private void initMesh() {

        float[] vertices = getVertices(160f);

        short[] indices = getIndices();

        VertexAttribute position =
                new VertexAttribute(
                        VertexAttributes.Usage.Position,
                        3,
                        "a_position"
                );

        VertexAttribute color =
                new VertexAttribute(
                        VertexAttributes.Usage.ColorUnpacked,
                        4,
                        "a_color"
                );

        mMesh =
                new Mesh(
                        true,
                        vertices.length / 7,
                        indices.length,
                        position,
                        color
                );

        mMesh.setVertices(vertices);

        mMesh.setIndices(indices);
    }

    private float[] getVertices(float r) {

        return new float[]{

                // x y z r g b a

                r, r, r, 1, 1, 1, 1,
                -r, r, r, 0, 1, 1, 1,
                -r, -r, r, 0, 0, 1, 1,
                r, -r, r, 1, 0, 1, 1,

                r, r, -r, 1, 1, 0, 1,
                -r, r, -r, 0, 1, 0, 1,
                -r, -r, -r, 0, 0, 0, 1,
                r, -r, -r, 1, 0, 0, 1
        };
    }

    private short[] getIndices() {

        return new short[]{

                // front
                0, 1, 2,
                0, 2, 3,

                // top
                0, 5, 1,
                0, 4, 5,

                // right
                0, 3, 7,
                0, 7, 4,

                // back
                6, 5, 4,
                6, 4, 7,

                // bottom
                6, 3, 2,
                6, 7, 3,

                // left
                6, 2, 1,
                6, 1, 5
        };
    }

    public void rotate(float x, float y) {

        this.rx = x;

        this.ry = y;
    }

    private void transform() {

        mModelMatrix.idt();

        mModelMatrix.rotate(Vector3.X, rx);

        mModelMatrix.rotate(Vector3.Y, ry);

        Matrix4 mvp =
                new Matrix4(mCamera.combined)
                        .mul(mModelMatrix);

        mShaderProgram.setUniformMatrix(
                "u_mvpTrans",
                mvp
        );
    }

    public void render() {

        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST);

        Gdx.gl.glClear(
                GL20.GL_DEPTH_BUFFER_BIT
        );

        mShaderProgram.begin();

        transform();

        mMesh.render(
                mShaderProgram,
                GL30.GL_TRIANGLES
        );

        mShaderProgram.end();

        Gdx.gl.glDisable(
                GL30.GL_DEPTH_TEST
        );
    }

    public void dispose() {

        mMesh.dispose();

        mShaderProgram.dispose();
    }
}
