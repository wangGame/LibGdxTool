package com.libGdx.test.shader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SimpleShader1 extends ApplicationAdapter {
    private ShaderProgram shader;
    private Mesh mesh;
    private float time;

    @Override
    public void create() {

        shader = new ShaderProgram(
                Gdx.files.internal("assets/shader/shengdanshu/1.v"),
                Gdx.files.internal("assets/shader/shengdanshu/1.f")
        );
        // 全屏四边形
        mesh = new Mesh(
                true,
                4,
                6,
                new VertexAttribute(
                        VertexAttributes.Usage.Position,
                        3,
                        "a_position"
                )
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
    }

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime() * 10;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shader.bind();
        shader.setUniformf("u_time", time);
//        shader.setUniformf("a", time * 0.1f);
        shader.setUniformf(
                "u_resolution",
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        mesh.render(shader, GL20.GL_TRIANGLES);
    }

    @Override
    public void dispose() {
        shader.dispose();
        mesh.dispose();
    }

    public static void main(String[] args) {
        new LwjglApplication(
                new SimpleShader1(),
                new LwjglApplicationConfiguration() {{
                    title = "Shader Demo";
                    width = 1280;
                    height = 720;
                }}
        );

    }
}