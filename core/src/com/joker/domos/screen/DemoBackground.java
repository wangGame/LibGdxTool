package com.joker.domos.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

public class DemoBackground extends Group implements Disposable {
    private FrameBuffer frameBuffer;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        ensureFrameBuffer();
        batch.end();
        frameBuffer.begin();
        Gdx.gl.glClearColor(0.07f, 0.09f, 0.16f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        super.draw(batch, parentAlpha);
        batch.end();
        frameBuffer.end();
        getStage().getViewport().apply();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    private void ensureFrameBuffer() {
        int width = Gdx.graphics.getBackBufferWidth();
        int height = Gdx.graphics.getBackBufferHeight();
        if (frameBuffer != null && frameBuffer.getWidth() == width && frameBuffer.getHeight() == height)
            return;
        if (frameBuffer != null) frameBuffer.dispose();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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
