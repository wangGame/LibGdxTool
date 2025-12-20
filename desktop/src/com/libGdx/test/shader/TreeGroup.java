package com.libGdx.test.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class TreeGroup extends Actor {
    private final ShaderProgram shader;
    private final Mesh mesh;
    private float time = 0f;

    public TreeGroup() {
        shader = new ShaderProgram(
                Gdx.files.internal("shader/shengdanshu/xxx.v"),
                Gdx.files.internal("shader/shengdanshu/yyy.f")
        );

        if (!shader.isCompiled()) {
            throw new RuntimeException(shader.getLog());
        }

        // 全屏 Mesh（NDC 空间）
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
                1f, 1f, 0f,
                -1f, 1f, 0f
        });

        mesh.setIndices(new short[]{
                0, 1, 2,
                2, 3, 0
        });

        // Actor 覆盖整个屏幕（逻辑尺寸）
        setSize(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        // ❗ Scene2D 的 batch 必须先停掉
        batch.end();
        shader.bind();
        shader.setUniformf("u_time", time);
        shader.setUniformf(
                "u_resolution",
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        Gdx.gl.glEnable(GL20.GL_BLEND);
        mesh.render(shader, GL20.GL_TRIANGLES);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // ❗ 恢复 batch，后面的 Actor 才能正常画
        batch.begin();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += Gdx.graphics.getDeltaTime();
    }
}