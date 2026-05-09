package com.libGdx.test.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.constant.Constant;

public class FrameBufferGroup extends Group {

    private FrameBuffer frameBuffer;
    private TextureRegion region;

    private int fboW;
    private int fboH;

    public FrameBufferGroup() {
        fboW = (int) Constant.GAMEWIDTH;
        fboH = (int) Constant.GAMEHIGHT;

        frameBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                fboW,
                fboH,
                false
        );

        region = new TextureRegion(frameBuffer.getColorBufferTexture());
        region.flip(false, true);

        setSize(Constant.GAMEWIDTH, Constant.GAMEHIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();

        Stage stage = getStage();
        Viewport viewport = stage == null ? null : stage.getViewport();

        int oldX = 0;
        int oldY = 0;
        int oldW = 0;
        int oldH = 0;

        if (viewport != null) {
            oldX = viewport.getScreenX();
            oldY = viewport.getScreenY();
            oldW = viewport.getScreenWidth();
            oldH = viewport.getScreenHeight();

            // 关键：让 ScrollPane 的 Scissor 按 FBO 尺寸计算
            viewport.setScreenBounds(0, 0, fboW, fboH);
        }

        frameBuffer.begin();

        Gdx.gl.glViewport(0, 0, fboW, fboH);

        // 透明背景
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.draw(batch, parentAlpha);

        batch.flush();

        frameBuffer.end();

        if (viewport != null) {
            // 恢复主 Stage 的 viewport
            viewport.setScreenBounds(oldX, oldY, oldW, oldH);
            viewport.apply(false);
        }
    }

    public TextureRegion getBufferTexture(float globalScale) {
        return region;
    }

    public void dispose() {
        if (frameBuffer != null) {
            frameBuffer.dispose();
            frameBuffer = null;
        }
    }
}