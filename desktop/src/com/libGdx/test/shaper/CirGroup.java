package com.libGdx.test.shaper;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;

public class CirGroup extends Group {
    private int blendSrcFunc = GL20.GL_SRC_ALPHA;
    private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
    private int blendSrcFuncAlpha = GL20.GL_SRC_ALPHA;
    private int blendDstFuncAlpha = GL20.GL_ONE_MINUS_SRC_ALPHA;
    private boolean showKong = true;

    protected ShapeRenderer sr;
    public CirGroup(){
        setPosition(200,200);
        sr = new ShapeRenderer();
    }

    public void setShowKong(boolean showKong) {
        this.showKong = showKong;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (showKong) {
            if (isTransform()) applyTransform(batch, computeTransform());
            batch.end();
            Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
            Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);
            sr.setProjectionMatrix(batch.getProjectionMatrix());
            sr.setTransformMatrix(batch.getTransformMatrix());
            sr.setColor(Color.valueOf("FF000000"));
            sr.begin(ShapeRenderer.ShapeType.Filled);
            drawCir();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            Gdx.gl.glStencilFunc(GL20.GL_NOTEQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);//没有通过测试的，保留原来的，也就是保留上一次的值。
            batch.begin();
            drawChildren(batch, parentAlpha);
            batch.flush();
            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
            Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 0, 0xFF);
            Gdx.gl.glDisable(Gdx.gl.GL_STENCIL_TEST);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT
                    | GL20.GL_STENCIL_BUFFER_BIT);
            if (isTransform()) resetTransform(batch);
        }else {
            super.draw(batch,parentAlpha);
        }
    }

    private float startAngle = 90;  // 0度，水平向右
    private float sweepAngle = 360;
    protected void drawCir(){

        sweepAngle -= 0.4f;  // 每次减小角度，表示逆向绘制圆弧
        if (sweepAngle <= 0) {
            sweepAngle = 0; // 防止角度小于0
        }



        // 圆心坐标
        float centerX = 71;
        float centerY = 71;

        // 圆的半径
        float radius = 90;


        sr.arc(centerX, centerY, radius, startAngle, sweepAngle);



    }
}
