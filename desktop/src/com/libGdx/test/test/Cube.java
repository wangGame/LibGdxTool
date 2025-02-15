package com.libGdx.test.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Cube extends ApplicationAdapter {
    private PerspectiveCamera mCamera;
    private ShaderProgram mShaderProgram;
    private Mesh mMesh;
    private Vector3 mRotateAxis; // 旋转轴
    private int mRotateAgree = 0; // 旋转角度
    Matrix4 mModelMatrix; // 模型变换矩阵

    @Override
    public void create() {
        initCamera();
        initShader();
        initMesh();
        mRotateAxis = new Vector3(0.3f, 0.5f, 0.7f);
        mModelMatrix = new Matrix4();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.455f, 0.725f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST);
        mShaderProgram.begin();
        transform();
        mMesh.render(mShaderProgram, GL30.GL_TRIANGLES);
    }

    @Override
    public void dispose() {
        mShaderProgram.dispose();
        mMesh.dispose();
    }

    private void initCamera() { // 初始化相机
        mCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mCamera.near = 0.3f;
        mCamera.far = 1000f;
        mCamera.position.set(0f, 0f, 2.5f);
        mCamera.lookAt(0, 0, 0);
        mCamera.update();
    }

    private void initShader() { // 初始化着色器程序
        String vertex = Gdx.files.internal("shader/cube.glsl").readString();
        String fragment = Gdx.files.internal("shader/cubef.glsl").readString();
        mShaderProgram = new ShaderProgram(vertex, fragment);
    }

    private void initMesh() { // 初始化网格
        float[] vertices = getVertices(0.5f, 1.0f);
        short[] indices = getIndices();
        VertexAttribute vertexPosition = new VertexAttribute(Usage.Position, 3, "a_position");
        VertexAttribute colorPosition = new VertexAttribute(Usage.ColorUnpacked, 4, "a_color");
        mMesh = new Mesh(true, vertices.length / 7, indices.length, vertexPosition, colorPosition);
        mMesh.setVertices(vertices);
        mMesh.setIndices(indices);
    }

    private void transform() { // MVP矩阵变换
        mRotateAgree = (mRotateAgree + 2) % 360;
        mModelMatrix.idt(); // 模型变换矩阵单位化
        mModelMatrix.rotate(mRotateAxis, mRotateAgree);
        Matrix4 mvpMatrix = mModelMatrix.mulLeft(mCamera.combined);
        mShaderProgram.setUniformMatrix("u_mvpTrans", mvpMatrix);
    }

    private float[] getVertices(float r, float c) { // 获取顶点数据
        float[] vertex = new float[] {
                r, r, r, c, c, c, 1, //0
                -r, r, r, 0, c, c, 1, //1
                -r, -r, r, 0, 0, c, 1, //2
                r, -r, r, c, 0, c, 1, //3
                r, r, -r, c, c, 0, 1, //4
                -r, r, -r, 0, c, 0, 1, //5
                -r, -r, -r, 0, 0, 0, 1, //6
                r, -r, -r, c, 0, 0, 1 //7
        };
        return vertex;
    }

    private short[] getIndices() { // 获取三角形顶点索引序列
        short[] indices = new short[] {
                0, 1, 2, 0, 2, 3, //前面
                0, 5, 1, 0, 4, 5, //上面
                0, 3, 7, 0, 7, 4, //右面
                6, 5, 4, 6, 4, 7, //后面
                6, 3, 2, 6, 7, 3, //下面
                6, 2, 1, 6, 1, 5 //左面
        };
        return indices;
    }
}