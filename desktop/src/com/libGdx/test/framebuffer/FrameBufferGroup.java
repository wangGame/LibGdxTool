package com.libGdx.test.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.constant.Constant;

import java.nio.ByteBuffer;

public class FrameBufferGroup extends Group {

    private final Actor actor;

    private FrameBuffer frameBuffer;
    private TextureRegion region;

    private final int fboW;
    private final int fboH;

    private final int captureW;
    private final int captureH;

    private final Matrix4 oldProjection = new Matrix4();
    private final Matrix4 oldTransform = new Matrix4();
    private final Matrix4 fboProjection = new Matrix4();

    private Image temp;
    private final Vector2 tempV2 = new Vector2();

    private boolean needUpdate = true;

    public FrameBufferGroup(ScrollPane scrollPane) {
        this.actor = scrollPane;

        /**
         * 这里保留 GAMEWIDTH / GAMEHIGHT。
         * 因为 ScrollPane 内部 Scissor 裁剪依赖 Stage viewport，
         * 用全屏 FBO 更不容易出裁剪错位。
         */
        this.fboW = (int) Constant.GAMEWIDTH;
        this.fboH = (int) Constant.GAMEHIGHT;
        pixelBuffer = BufferUtils.newByteBuffer(
                fboW * fboH * 4
        );
        /**
         * 真正想截图的区域大小：ScrollPane 原始宽高。
         * 注意：这里不要乘 scale。
         */
        this.captureW = (int) scrollPane.getWidth();
        this.captureH = (int) scrollPane.getHeight();

        frameBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                fboW,
                fboH,
                false
        );

        region = new TextureRegion(frameBuffer.getColorBufferTexture());

        /**
         * 因为 FBO 的图是上下反的。
         *
         * 我们把 ScrollPane 画在 FBO 的左下角，
         * 所以裁剪时要从 FBO 的底部区域取。
         *
         * TextureRegion 的 y 是按“上方”为 0 的习惯来算，
         * 所以底部 captureH 区域对应：
         *
         * y = fboH - captureH
         */
        updateRegion();

        setSize(Constant.GAMEWIDTH, Constant.GAMEHIGHT);

        /**
         * ScrollPane 作为子节点保留，用来接收触摸。
         * 但是 FrameBufferGroup.draw() 不会把它直接画到屏幕上。
         */
        addActor(actor);
    }
    private void readFrameBufferPixels() {

        pixelBuffer.clear();


        Gdx.gl.glReadPixels(
                0,
                0,
                fboW,
                fboH,
                GL20.GL_RGBA,
                GL20.GL_UNSIGNED_BYTE,
                pixelBuffer
        );


        pixelBuffer.position(0);


        System.out.println(
                "capacity = "
                        + pixelBuffer.capacity()
        );
    }
    private void saveFrame() {


        Pixmap pixmap =
                new Pixmap(
                        fboW,
                        fboH,
                        Pixmap.Format.RGBA8888
                );


        pixmap.getPixels()
                .put(pixelBuffer);


        pixmap.getPixels().flip();




        xx++;
        PixmapIO.writePNG(
                Gdx.files.local("capture"+xx+".png"),
                pixmap
        );


        pixmap.dispose();
    }
    private int xx = 0;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        updateActorHitAreaFromTemp();

        drawActorToFrameBuffer(batch, parentAlpha);

        /**
         * 注意：这里不要 super.draw(batch, parentAlpha);
         *
         * 否则 ScrollPane 会被正常画到屏幕上。
         * 现在屏幕上显示的是外面的 temp Image。
         */
    }

    private void drawActorToFrameBuffer(Batch batch, float parentAlpha) {
        validateActorLayout();

        batch.flush();

        StageViewportBackup viewportBackup = backupViewport();

        /**
         * 保存 batch 原矩阵。
         */
        oldProjection.set(batch.getProjectionMatrix());
        oldTransform.set(batch.getTransformMatrix());

        /**
         * 保存 actor 原来的状态。
         * 因为 actor 在舞台上要负责触摸位置，
         * 但截图时我们要把它放到 FBO 左下角。
         */
        float oldX = actor.getX();
        float oldY = actor.getY();
        float oldScaleX = actor.getScaleX();
        float oldScaleY = actor.getScaleY();
        float oldRotation = actor.getRotation();
        float oldOriginX = actor.getOriginX();
        float oldOriginY = actor.getOriginY();

        actor.setPosition(0, 0);
        actor.setOrigin(0, 0);
        actor.setScale(1f, 1f);
        actor.setRotation(0f);

        frameBuffer.begin();

        Gdx.gl.glViewport(0, 0, fboW, fboH);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /**
         * FBO 内使用自己的正交投影。
         */
        fboProjection.setToOrtho2D(0, 0, fboW, fboH);
        batch.setProjectionMatrix(fboProjection);
        batch.setTransformMatrix(new Matrix4());

        batch.flush();

        /**
         * 直接画 actor，不走 super.draw。
         */
        actor.draw(batch, parentAlpha);

        batch.flush();
        readFrameBufferPixels();
        frameBuffer.end();
// GPU -> CPU


        saveFrame();
        /**
         * 恢复 actor 状态。
         */
        actor.setPosition(oldX, oldY);
        actor.setOrigin(oldOriginX, oldOriginY);
        actor.setScale(oldScaleX, oldScaleY);
        actor.setRotation(oldRotation);

        /**
         * 恢复 batch 矩阵。
         */
        batch.setProjectionMatrix(oldProjection);
        batch.setTransformMatrix(oldTransform);
        batch.flush();

        restoreViewport(viewportBackup);
    }

    private void updateRegion() {
        region.setRegion(
                0,
                fboH - captureH,
                captureW,
                captureH
        );

        /**
         * setRegion 会重置 UV，所以这里每次 setRegion 后都要 flip。
         */
        region.flip(false, true);
    }

    private void validateActorLayout() {
        if (actor instanceof Layout) {
            ((Layout) actor).validate();
        }

        if (actor instanceof Group) {
            Group group = (Group) actor;
            for (int i = 0; i < group.getChildren().size; i++) {
                Actor child = group.getChildren().get(i);
                if (child instanceof Layout) {
                    ((Layout) child).validate();
                }
            }
        }
    }

    private void updateActorHitAreaFromTemp() {
        if (temp == null) return;

        /**
         * 把 temp 的中心点转成 FrameBufferGroup 的局部坐标，
         * 然后把 ScrollPane 放到这个位置。
         */
        float centerX = temp.getX(Align.center);
        float centerY = temp.getY(Align.center);

        tempV2.set(centerX, centerY);
        temp.getParent().localToStageCoordinates(tempV2);
        this.stageToLocalCoordinates(tempV2);

        actor.setOrigin(Align.center);
        actor.setPosition(tempV2.x, tempV2.y, Align.center);

        /**
         * 让 ScrollPane 的触摸区域和 temp 的显示尺寸一致。
         *
         * temp 显示多大，ScrollPane 的交互区域就多大。
         */
        actor.setScale(
                temp.getWidth() / captureW,
                temp.getHeight() / captureH
        );
    }

    private StageViewportBackup backupViewport() {
        StageViewportBackup backup = new StageViewportBackup();

        if (getStage() == null) return backup;

        Viewport viewport = getStage().getViewport();
        backup.viewport = viewport;
        backup.x = viewport.getScreenX();
        backup.y = viewport.getScreenY();
        backup.w = viewport.getScreenWidth();
        backup.h = viewport.getScreenHeight();

        /**
         * 让 ScrollPane 的 Scissor 按 FBO 尺寸算。
         */
        viewport.setScreenBounds(0, 0, fboW, fboH);

        return backup;
    }
    private ByteBuffer pixelBuffer;
    private void restoreViewport(StageViewportBackup backup) {
        if (backup.viewport == null) return;

        backup.viewport.setScreenBounds(
                backup.x,
                backup.y,
                backup.w,
                backup.h
        );

        backup.viewport.apply(false);
    }

    public TextureRegion getBufferTexture(float globalScale) {
        updateRegion();
        return region;
    }

    public void setDrawContent(Image temp) {
        this.temp = temp;
        this.needUpdate = true;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void dispose() {
        if (frameBuffer != null) {
            frameBuffer.dispose();
            frameBuffer = null;
        }
    }

    private static class StageViewportBackup {
        Viewport viewport;
        int x;
        int y;
        int w;
        int h;
    }
}