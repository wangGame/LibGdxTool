package com.kw.gdx.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PolygonClipGroup extends Group {
    private Polygon polygon;
    private ShapeRenderer shapeRenderer;

    public PolygonClipGroup(ShapeRenderer shapeRenderer){
        this.shapeRenderer = shapeRenderer;
        shapeRenderer.setAutoShapeType(true);
        polygon = new Polygon();
        polygon.setVertices(new float[]{0,0,0,100,100,100,100,0});
        setPosition(200,200);
    }
    private int blendSrcFunc = GL20.GL_SRC_ALPHA;
    private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
    private int blendSrcFuncAlpha = GL20.GL_SRC_ALPHA;
    private int blendDstFuncAlpha = GL20.GL_ONE_MINUS_SRC_ALPHA;


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isTransform()) applyTransform(batch, computeTransform());
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        batch.end();
        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.polygon(polygon.getVertices());
        shapeRenderer.setColor(new Color(255f / 255f, 255f / 255f, 255.0f / 255f, 1f));


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);

        shapeRenderer.circle(0,0,200);


        Gdx.gl.glDisable(GL20.GL_BLEND);

        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);//没有通过测试的，保留原来的，也就是保留上一次的值。
        batch.begin();
        drawChildren(batch, parentAlpha);
        batch.flush();
        Gdx.gl.glDisable(Gdx.gl.GL_STENCIL_TEST);
        if (isTransform()) resetTransform(batch);
    }
}
