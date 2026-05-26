package com.libGdx.test.alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * 透明度是受混合模式影响的
 *
 * 可以关闭混合模式
 */
public class AlphaTest  extends Group {
    public AlphaTest() {
        Image image = new Image(new Texture("assets/3_34_24.png"));
        addActor(image);
        image.getColor().a = 0.5f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();

        boolean oldBlend = batch.isBlendingEnabled();

        batch.disableBlending();
        Gdx.gl.glColorMask(true, false, false, false);

        super.draw(batch, parentAlpha);

        batch.flush();

        Gdx.gl.glColorMask(true, true, true, true);

        if (oldBlend) {
            batch.enableBlending();
        } else {
            batch.disableBlending();
        }
    }
}