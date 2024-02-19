package com.kw.gdx.clip;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PolygonClipGroup extends Group {
    private ShapeRenderer shapeRenderer;
    private int blendSrcFunc = 770;
    private int blendDstFunc = 771;
    private int blendSrcFuncAlpha = 770;
    private int blendDstFuncAlpha = 771;

    public PolygonClipGroup(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
        shapeRenderer.setAutoShapeType(true);
        this.setPosition(200.0F, 200.0F);
    }

    public void draw(Batch batch, float parentAlpha) {
        if (this.isTransform()) {
            this.applyTransform(batch, this.computeTransform());
        }

        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        batch.end();
        Gdx.gl.glEnable(2960);
        Gdx.gl.glStencilOp(7680, 7680, 7681);
        Gdx.gl.glStencilFunc(519, 1, 255);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFuncSeparate(this.blendSrcFunc, this.blendDstFunc, this.blendSrcFuncAlpha, this.blendDstFuncAlpha);
        this.shapeRenderer.circle(0.0F, 0.0F, 200.0F);
        Gdx.gl.glDisable(3042);
        Gdx.gl.glStencilFunc(514, 1, 255);
        Gdx.gl.glStencilOp(7680, 7680, 7680);
        batch.begin();
        this.drawChildren(batch, parentAlpha);
        batch.flush();
        Gdx.gl.glDisable(2960);
        if (this.isTransform()) {
            this.resetTransform(batch);
        }

    }
}
