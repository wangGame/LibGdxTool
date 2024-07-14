package com.kw.gdx.clip;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ClipActor extends Actor {
    private Image img;
    private Image mask;

    public ClipActor(TextureRegion region,TextureRegion region1) {
        mask = new Image(region);
        img = new Image(region1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawAlphaMask(batch);
        //画前景色
        drawForeground(batch, 0, 0, (int) mask.getWidth(), (int) mask.getHeight());
    }

    private void drawForeground(Batch batchPara, int clipX, int clipY, int clipWidth, int clipHeight) {
        Gdx.gl.glColorMask(true, true, true, true);
        batchPara.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
        img.draw(batchPara, 1f);
        batchPara.flush();

    }

    private void drawAlphaMask(Batch batch) {
        Gdx.gl.glColorMask(false, false, false, true);
        mask.draw(batch, 1);
        batch.flush();
    }
}