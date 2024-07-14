package com.libGdx.test.alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AlphaTest extends Group {
    private Image image;

    public AlphaTest() {
        image = new Image(new Texture("assets/3_34_24.png"));
        addActor(image);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glEnable(GL20.GL_ALPHA);
        batch.flush();
        Gdx.gl.glColorMask(false, false, true, true);
        super.draw(batch, parentAlpha);

        batch.flush();
        Gdx.gl.glDisable(GL20.GL_ALPHA);
    }
}