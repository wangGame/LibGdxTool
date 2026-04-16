package com.libGdx.test.shaper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;

public class DepthGroup extends Group {
    private int blendSrcFunc = GL20.GL_SRC_ALPHA;
    private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
    private int blendSrcFuncAlpha = GL20.GL_SRC_ALPHA;
    private int blendDstFuncAlpha = GL20.GL_ONE_MINUS_SRC_ALPHA;
    private boolean quf;
    protected ShapeRenderer sr;
    private boolean startModelTest;
    public DepthGroup(ShapeRenderer sr){
        this.sr = sr;
    }

    public boolean isStartModelTest() {
        return startModelTest;
    }

    public void setStartModelTest(boolean startModelTest) {
        this.startModelTest = startModelTest;
    }

    public void setQuf(boolean quf) {
        this.quf = quf;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (isTransform()) applyTransform(batch, computeTransform());
        batch.end();
        Gdx.gl.glClearDepthf(1.0f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        // 设置 GL 状态：只写深度，不写颜色
        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);


        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setTransformMatrix(batch.getTransformMatrix());
        sr  .setColor(Color.valueOf("00000000"));
        sr.begin(ShapeRenderer.ShapeType.Filled);
        drawCir();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);
        sr.end();

        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL); // 核心：只在刚才画过的形状上绘制


        batch.begin();
        super.draw(batch,parentAlpha);
        batch.flush();



        // --- 6. 清理状态 (Phase 3: Cleanup) ---
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT
                | GL20.GL_STENCIL_BUFFER_BIT);
        if (isTransform()) resetTransform(batch);
    }

    protected void drawCir(){
        sr.circle(0,00,5);
    }
}
