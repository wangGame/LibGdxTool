package com.libGdx.test.sen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/26 7:27
 */
public class Cir extends Actor {
    private float centerX;
    private float centerY;
    private float radius;
    private ShapeRenderer sr;

    public Cir(float x, float y, float radius) {
        this.centerX = x;
        this.centerY = y;
        this.radius = radius;
        setPosition(centerX - radius, centerY - radius);
        setSize(radius * 2, radius * 2);
        sr = new ShapeRenderer();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (sr != null) {
            batch.end();
            sr.setProjectionMatrix(batch.getProjectionMatrix());
            sr.setTransformMatrix(batch.getTransformMatrix());
            sr.setColor(Color.valueOf("FFFFFF00"));
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.circle(centerX, centerY, radius);
            sr.end();
            batch.begin();
        }
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}
