package com.libGdx.test.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    public short id;
    protected float angle;
    public boolean destroy;
    public boolean remove;
    protected float vX, vY;
    protected short extra;
    private Vector2 tempVector;

    public Entity(short id, float x, float y){
        tempVector = new Vector2();
        this.id = id;
        destroy = false;
        remove = false;
    }

    public abstract void render(float delta, Batch batch);

    public abstract void dispose();

    public void drawAll(Sprite sprite, Batch batch, float x, float y) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
        if (x > 100 / 2) {
            sprite.setPosition(x - 100, y);
        } else {
            sprite.setPosition(x + 100, y);
        }
        sprite.draw(batch);

        if (y > 100 / 2) {
            sprite.setPosition(x, y - 100);
        } else {
            sprite.setPosition(x, y + 100);
        }
        sprite.draw(batch);
    }

}
