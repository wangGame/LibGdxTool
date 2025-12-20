package com.libGdx.test.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.constant.Constant;

public class ShaderImage extends Image {

    private static Texture DUMMY;

    private final ShaderProgram shader;
    private float time = 0f;

    public ShaderImage() {
        super(getDummyTexture());

        shader = new ShaderProgram(
                Gdx.files.internal("shader/shaderimage/xxx.v"),
                Gdx.files.internal("shader/shaderimage/yyy.f")
        );

        if (!shader.isCompiled()) {
            throw new RuntimeException(shader.getLog());
        }

        setDebug(true);
    }

    private static Texture getDummyTexture() {
        if (DUMMY == null) {
            Pixmap pm = new Pixmap((int) Constant.GAMEWIDTH, (int) Constant.GAMEHIGHT, Pixmap.Format.RGBA8888);
            pm.setColor(Color.WHITE);
            pm.fill();
            DUMMY = new Texture(pm);
            pm.dispose();
        }
        return DUMMY;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ShaderProgram old = batch.getShader();
        batch.setShader(shader);
        shader.bind();
        shader.setUniformf("u_time", time);
        shader.setUniformf(
                "u_resolution",
                getWidth(),
                getHeight()
        );

        super.draw(batch, parentAlpha);
        batch.setShader(old);
    }
}