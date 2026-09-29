package com.joker.domos.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.kw.gdx.asset.Asset;

public class FpsDisplay extends Actor implements Disposable {
    private BitmapFont font;

    public FpsDisplay() {
        this.font = Asset.getAsset().loadBitFont("font/Cali_75.fnt");
        this.font.getData().setScale(2f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.font.setColor(1f, 1f, 1f, parentAlpha);
        this.font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), getX(), getY() + getHeight(), getWidth(), Align.right, false);
    }

    @Override
    public void dispose() {
        this.font.dispose();
    }
}
