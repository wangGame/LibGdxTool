package com.kw.gdx.shape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.asset.Asset;

public class CirGroup extends Group {

    protected ShapeRenderer sr;

    /**
     * 是否开启遮罩
     */
    private boolean startModelTest;

    /**
     * false：显示遮罩内部
     * true ：显示遮罩外部
     */
    private boolean quf;

    public CirGroup(ShapeRenderer shapeRenderer) {
        this.sr = shapeRenderer;
    }

    public boolean isStartModelTest() {
        return startModelTest;
    }

    public void setStartModelTest(boolean startModelTest) {
        this.startModelTest = startModelTest;
    }

    public boolean isQuf() {
        return quf;
    }

    public void setQuf(boolean quf) {
        this.quf = quf;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // 不开启遮罩，走正常 Group 绘制
        if (!startModelTest) {
            super.draw(batch, parentAlpha);
            return;
        }

        /*
         * Group 默认 draw() 里面会处理 transform。
         *
         * 因为这里没有调用 super.draw()，
         * 所以 transform 需要我们自己处理。
         */
        if (isTransform()) {
            applyTransform(batch, computeTransform());
        }

        // ============================================================
        // 1. SpriteBatch 结束
        // ============================================================

        batch.end();

        // ============================================================
        // 2. 开启 Stencil Test
        // ============================================================

        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);

        /*
         * 每次开始一个新的 Mask 前，
         * 把 stencil buffer 清成 0。
         */
        Gdx.gl.glClearStencil(0);
        Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);

        // ============================================================
        // 3. 写入 Mask
        // ============================================================

        /*
         * 允许 stencil buffer 所有 bit 被修改。
         */
        Gdx.gl.glStencilMask(0xFF);

        /*
         * Stencil 测试永远通过。
         *
         * 注意这里的 1 是 reference value。
         *
         * 后面的 GL_REPLACE 会让：
         *
         * stencil = 1
         *
         * 不是：
         *
         * stencil = stencil + 1
         */
        Gdx.gl.glStencilFunc(
                GL20.GL_ALWAYS,
                1,
                0xFF
        );

        /*
         * stencil test 通过之后：
         *
         * stencil = reference
         *
         * 也就是 stencil = 1
         */
        Gdx.gl.glStencilOp(
                GL20.GL_KEEP,
                GL20.GL_KEEP,
                GL20.GL_REPLACE
        );

        /*
         * 非常重要：
         *
         * 画圆只是为了修改 Stencil Buffer，
         * 不需要真的画到 Color Buffer。
         */
        Gdx.gl.glColorMask(
                false,
                false,
                false,
                false
        );

        // ShapeRenderer 使用和当前 Batch 一样的矩阵
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setTransformMatrix(batch.getTransformMatrix());

        sr.begin(ShapeRenderer.ShapeType.Filled);

        drawCir();

        sr.end();

        /*
         * 恢复正常颜色写入。
         */
        Gdx.gl.glColorMask(
                true,
                true,
                true,
                true
        );

        // ============================================================
        // 4. 开始使用 Mask
        // ============================================================

        /*
         * 从这里开始：
         * 不允许子节点修改 Stencil Buffer。
         */
        Gdx.gl.glStencilMask(0x00);

        if (quf) {

            /*
             * 反向 Mask：
             *
             * stencil != 1
             *
             * 也就是圆外显示。
             */
            Gdx.gl.glStencilFunc(
                    GL20.GL_NOTEQUAL,
                    1,
                    0xFF
            );

        } else {

            /*
             * 正常 Mask：
             *
             * stencil == 1
             *
             * 也就是圆内显示。
             */
            Gdx.gl.glStencilFunc(
                    GL20.GL_EQUAL,
                    1,
                    0xFF
            );
        }

        /*
         * 绘制子节点期间完全不修改 Stencil。
         */
        Gdx.gl.glStencilOp(
                GL20.GL_KEEP,
                GL20.GL_KEEP,
                GL20.GL_KEEP
        );

        // ============================================================
        // 5. 绘制 Group 的 children
        // ============================================================

        batch.begin();

        drawChildren(batch, parentAlpha);

        /*
         * 非常重要。
         *
         * 后面马上要修改 OpenGL stencil 状态，
         * 所以必须先把 SpriteBatch 当前缓存的顶点提交出去。
         */
        batch.flush();

        // ============================================================
        // 6. 恢复 OpenGL 状态
        // ============================================================

        /*
         * 恢复 stencil 写权限。
         *
         * 虽然马上关闭 stencil，
         * 但恢复一下比较安全。
         */
        Gdx.gl.glStencilMask(0xFF);

        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);

        // ============================================================
        // 7. 恢复 Group Transform
        // ============================================================

        if (isTransform()) {
            resetTransform(batch);
        }
    }

    /**
     * 子类重写这里绘制遮罩形状。
     *
     * 注意：
     * 这里画出来的东西不会真正显示在屏幕上，
     * 只是写入 Stencil Buffer。
     */
    protected void drawCir() {

    }
}




























//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.scenes.scene2d.Group;
//
//public class CirGroup extends Group {
//    private int blendSrcFunc = GL20.GL_SRC_ALPHA;
//    private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
//    private int blendSrcFuncAlpha = GL20.GL_SRC_ALPHA;
//    private int blendDstFuncAlpha = GL20.GL_ONE_MINUS_SRC_ALPHA;
//    private boolean quf;
//    protected ShapeRenderer sr;
//    private boolean startModelTest;
//    public CirGroup(ShapeRenderer sr){
//        this.sr = sr;
//    }
//
//    public boolean isStartModelTest() {
//        return startModelTest;
//    }
//
//    public void setStartModelTest(boolean startModelTest) {
//        this.startModelTest = startModelTest;
//    }
//
//    public void setQuf(boolean quf) {
//        this.quf = quf;
//    }
//
//    @Override
//    public void act(float delta) {
//        super.act(delta);
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        if (startModelTest) {
//            if (isTransform()) applyTransform(batch, computeTransform());
//            batch.end();
//            Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
//            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
//            Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);
//            sr.setProjectionMatrix(batch.getProjectionMatrix());
//            sr.setTransformMatrix(batch.getTransformMatrix());
//            sr  .setColor(Color.valueOf("00000000"));
//            sr.begin(ShapeRenderer.ShapeType.Filled);
//            drawCir();
//            Gdx.gl.glEnable(GL20.GL_BLEND);
//            Gdx.gl.glBlendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);
//            sr.end();
//            Gdx.gl.glDisable(GL20.GL_BLEND);
//            if (quf) {
//                Gdx.gl.glStencilFunc(GL20.GL_NOTEQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
//            }else {
//                Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
//            }
//            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);//没有通过测试的，保留原来的，也就是保留上一次的值。
//            batch.begin();
//            drawChildren(batch, parentAlpha);
//            batch.flush();
//            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
//            Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 0, 0xFF);
//            Gdx.gl.glDisable(Gdx.gl.GL_STENCIL_TEST);
//            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT
//                    | GL20.GL_STENCIL_BUFFER_BIT);
//            if (isTransform()) resetTransform(batch);
//        }else {
//            super.draw(batch,parentAlpha);
//        }
//    }
//
//    protected void drawCir(){
//
//    }
//}
