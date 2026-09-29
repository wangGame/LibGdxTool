package com.joker.domos.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

public class DemoBackground extends Group implements Disposable {
    private FrameBuffer frameBuffer;
    private final Matrix4 captureProjection = new Matrix4();
    private final Vector2 captureBottomLeft = new Vector2();
    private final Vector2 captureTopRight = new Vector2();
    private Actor captureActor;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        updateCaptureBounds();
        if (captureActor == null || captureBottomLeft.x >= captureTopRight.x || captureBottomLeft.y >= captureTopRight.y) {
            super.draw(batch, parentAlpha);
            return;
        }

        float pixelsPerWorldX = getStage().getViewport().getScreenWidth() / getStage().getViewport().getWorldWidth();
        float pixelsPerWorldY = getStage().getViewport().getScreenHeight() / getStage().getViewport().getWorldHeight();
        int captureWidth = Math.max(1, Math.round((captureTopRight.x - captureBottomLeft.x) * pixelsPerWorldX));
        int captureHeight = Math.max(1, Math.round((captureTopRight.y - captureBottomLeft.y) * pixelsPerWorldY));
        ensureFrameBuffer(captureWidth, captureHeight);

        Matrix4 stageProjection = new Matrix4(batch.getProjectionMatrix());
        batch.end();
        frameBuffer.begin();
        Gdx.gl.glClearColor(0.07f, 0.09f, 0.16f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        captureProjection.setToOrtho2D(
                captureBottomLeft.x,
                captureBottomLeft.y,
                captureTopRight.x - captureBottomLeft.x,
                captureTopRight.y - captureBottomLeft.y);
        batch.setProjectionMatrix(captureProjection);
        batch.begin();
        super.draw(batch, parentAlpha);
        batch.end();
        frameBuffer.end();
        getStage().getViewport().apply();
        batch.setProjectionMatrix(stageProjection);
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    private void updateCaptureBounds() {
        if (captureActor == null || captureActor.getStage() == null) return;
        captureBottomLeft.set(0f, 0f);
        captureTopRight.set(captureActor.getWidth(), captureActor.getHeight());
        captureActor.localToStageCoordinates(captureBottomLeft);
        captureActor.localToStageCoordinates(captureTopRight);
    }

    private void ensureFrameBuffer(int width, int height) {
        if (frameBuffer != null && frameBuffer.getWidth() == width && frameBuffer.getHeight() == height)
            return;
        if (frameBuffer != null) frameBuffer.dispose();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setCaptureActor(Actor captureActor) {
        this.captureActor = captureActor;
    }

    public Texture getCaptureTexture() {
        return frameBuffer.getColorBufferTexture();
    }

    public int getCaptureWidth() {
        return frameBuffer.getWidth();
    }

    public int getCaptureHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void dispose() {
        if (frameBuffer != null) frameBuffer.dispose();

    }
}
