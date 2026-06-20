package com.libGdx.test.shader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class SimpleShader2 extends ApplicationAdapter {

    private ShaderProgram shader;
    private Mesh mesh;

    private final Array<Vector2> points = new Array<>();

    @Override
    public void create() {

        shader = new ShaderProgram(
                Gdx.files.internal("assets/shader/shengdanshu/1.v"),
                Gdx.files.internal("assets/shader/shengdanshu/Beizer_2.frag")
        );

        if (!shader.isCompiled()) {
            System.out.println(shader.getLog());
        }

        mesh = new Mesh(
                true,
                4,
                6,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position")
        );

        mesh.setVertices(new float[]{
                -1f, -1f, 0f,
                1f, -1f, 0f,
                1f,  1f, 0f,
                -1f,  1f, 0f
        });

        mesh.setIndices(new short[]{
                0, 1, 2,
                2, 3, 0
        });

        // 点击加点
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                float x = (screenX / (float)Gdx.graphics.getWidth());
                float y = 1.0f - (screenY / (float)Gdx.graphics.getHeight());

                float px = (x - 0.5f) * Gdx.graphics.getWidth();
                float py = (y - 0.5f) * Gdx.graphics.getHeight();

                points.add(new Vector2(px, py));

                return true;
            }
        });
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shader.bind();

        shader.setUniformf(
                "u_resolution",
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        // ===== 转 float[] =====
        float[] arr = new float[points.size * 2];

        for (int i = 0; i < points.size; i++) {
            arr[i * 2] = points.get(i).x;
            arr[i * 2 + 1] = points.get(i).y;
        }

        // ===== 传点 =====
        shader.setUniform2fv("u_points", arr, 0, arr.length);

        shader.setUniformi("u_pointCount", points.size);

        mesh.render(shader, GL20.GL_TRIANGLES);
    }

    @Override
    public void dispose() {
        shader.dispose();
        mesh.dispose();
    }

    public static void main(String[] args) {
        new LwjglApplication(
                new SimpleShader2(),
                new LwjglApplicationConfiguration() {{
                    title = "Shader Demo";
                    width = 1280;
                    height = 720;
                }}
        );

    }
}