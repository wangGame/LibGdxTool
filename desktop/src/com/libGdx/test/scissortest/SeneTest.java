package com.libGdx.test.scissortest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.stencil.Cir;

public class SeneTest extends Group {
    private Image image;
    public SeneTest() {
        image = new Image(new Texture("assets/3_34_24.png"));
        addActor(image);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(10,10,100,100);
        batch.flush();
        super.draw(batch,parentAlpha);
        batch.flush();
        Gdx.gl.glDisable(Gdx.gl.GL_SCISSOR_TEST);
    }
}