package com.libGdx.test.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kw.gdx.asset.Asset;

public class EntityApp extends Entity{
    private Sprite sprite;
    private Vector2 position;
    public EntityApp(short id, float x, float y) {
        super(id, x, y);
        this.position = new Vector2(x, y);
        sprite = Asset.getAsset().getSprite("assets/7.png");
    }

    @Override
    public void render(float delta, Batch batch) {
        sprite.setRotation(angle * MathUtils.radiansToDegrees);

        float x = position.x - sprite.getWidth() / 2;
        float y = position.y - sprite.getHeight() / 2;
        drawAll(sprite, batch, x, y);
    }

    @Override
    public void dispose() {

    }
}
