package com.libGdx.test.colorcircle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import com.kw.gdx.asset.Asset;

/**
 * 不是高级东西，  就是单纯的TextureRegion切割
 */
public class ColorCircle extends Widget {
    private final TextureRegion chip;
    private final static int SIZE = 18;
    private final static int BORDER = 4;

    public ColorCircle(int color) {
        Texture texture = Asset.getAsset().getTexture("assets/textures/colors.png");
        this.chip = new TextureRegion(texture, color * SIZE +SIZE, 0, SIZE,SIZE);
    }

    @Override
    public float getPrefHeight() {
        return SIZE + BORDER * 2;
    }

    @Override
    public float getPrefWidth() {
        return SIZE+ BORDER * 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(chip, getX()+BORDER, getY()+BORDER);
    }
}

