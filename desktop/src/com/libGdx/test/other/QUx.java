package com.libGdx.test.other;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/28 16:46
 */
class QUx extends ApplicationAdapter {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.y = 0;
        config.height = (int) (900 );
        config.width = (int) (980  );
        Gdx.isJiami = true;
        QUx dekstop = new QUx();
        new LwjglApplication(dekstop, config);
    }

    private ShaderProgram shaderProgram;
    private Mesh mesh;
    private Texture texture;
    private int numVertices = 50; // 曲线上的顶点数量

    @Override
    public void create() {
        String vertexShader = "attribute vec2 a_position; \n"
                + "attribute vec2 a_texCoord; \n"
                + "varying vec2 v_texCoord; \n"
                + "void main() { \n"
                + "    gl_Position = vec4(a_position, 0.0, 1.0); \n"
                + "    v_texCoord = a_texCoord; \n"
                + "}";
        String fragmentShader = "uniform sampler2D u_texture; \n"
                + "varying vec2 v_texCoord; \n"
                + "void main() { \n"
                + "    gl_FragColor = texture2D(u_texture, v_texCoord); \n"
                + "}";

        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

        mesh = createCurveMesh();
        texture = new Texture(Gdx.files.internal("texture.png"));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderProgram.begin();
        shaderProgram.setUniformi("u_texture", 0);
        texture.bind();

        mesh.render(shaderProgram, GL20.GL_TRIANGLES);

        shaderProgram.end();
    }



    private Mesh createCurveMesh() {
        int numSegments = numVertices - 1;

        float[] vertices = new float[numVertices * 2];
        float[] texCoords = new float[numVertices * 2];
        short[] indices = new short[numSegments * 6];

        // 定义曲线的控制点
        Vector2 startPoint = new Vector2(00, 00);
        Vector2 controlPoint = new Vector2(150, 50);
        Vector2 endPoint = new Vector2(100, 100);

        float t = 0;
        float dt = 1f / numSegments;

        for (int i = 0; i < numVertices; i++) {
            float x = calculateBezierPoint(startPoint.x, controlPoint.x, endPoint.x, t);
            float y = calculateBezierPoint(startPoint.y, controlPoint.y, endPoint.y, t);

            vertices[i * 2] = x; // x 坐标
            vertices[i * 2 + 1] = y; // y 坐标

            texCoords[i * 2] = t; // 纹理坐标（u）
            texCoords[i * 2 + 1] = 0; // 纹理坐标（v）

            t += dt;
        }

        for (int i = 0; i < numSegments; i++) {
            indices[i * 6] = (short) i;
            indices[i * 6 + 1] = (short) (i + 1);
            indices[i * 6 + 2] = (short) (i + 2);
            indices[i * 6 + 3] = (short) (i + 2);
            indices[i * 6 + 4] = (short) (i + 1);
            indices[i * 6 + 5] = (short) (i + 3);
        }

        Mesh mesh = new Mesh(true, numVertices, numSegments * 6,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord"));
        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        return mesh;
    }

    private float calculateBezierPoint(float p0, float p1, float p2, float t) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;
        return uuu * p0 + 3 * uu * t * p1 + 3 * u * tt * p2 + ttt;
    }

}
