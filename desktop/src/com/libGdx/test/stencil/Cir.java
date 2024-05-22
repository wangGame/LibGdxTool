package com.libGdx.test.stencil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
    private int blendSrcFunc = GL20.GL_SRC_ALPHA;
    private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
    private int blendSrcFuncAlpha = GL20.GL_SRC_ALPHA;
    private int blendDstFuncAlpha = GL20.GL_ONE_MINUS_SRC_ALPHA;

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (sr != null) {
            batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);


            sr.setProjectionMatrix(batch.getProjectionMatrix());
            sr.setTransformMatrix(batch.getTransformMatrix());
            sr.setColor(Color.valueOf("000000"));
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.circle(centerX, centerY, radius);

            Gdx.gl.glDisable(GL20.GL_BLEND);


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
