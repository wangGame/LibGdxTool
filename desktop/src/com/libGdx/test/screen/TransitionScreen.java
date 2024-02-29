package com.libGdx.test.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class TransitionScreen implements Screen {
    // Rendering
    private FrameBuffer frameBuffer;
    private TextureRegion bufferTexture;
    private final SpriteBatch spriteBatch;
    private float fadedElapsed;
    private boolean fadingOut;
    private int width, height;
    // From, to, and game to change the screen after the transition finishes
    private final Screen fromScreen, toScreen;
    // Should the previous screen be disposed afterwards? Not desirable
    // if it was stored somewhere else, for example, to return to it later
    private final boolean disposeAfter;
    // Time it takes to fade out and in, 0.15s (0.3s total)
    private static final float FADE_INVERSE_DELAY = 1f / 0.15f;

    public TransitionScreen(Screen from, Screen to, boolean disposeAfter) {
        this.disposeAfter = disposeAfter;
        fromScreen = from;
        toScreen = to;
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        fadedElapsed = 0f;
        fadingOut = true;
    }

    @Override
    public void render(float delta) {
        // Black background since we're fading to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        frameBuffer.begin();

        float opacity;
        if (fadingOut) {
            fromScreen.render(delta);
            opacity = 1 - Math.min(fadedElapsed * FADE_INVERSE_DELAY, 1);
            if (opacity == 0) {
                fadedElapsed = 0;
                fadingOut = false;
            }
        } else {
            toScreen.render(delta);
            opacity = Math.min(fadedElapsed * FADE_INVERSE_DELAY, 1);
        }

        frameBuffer.end();

        // Render the faded texture
        spriteBatch.begin();
        spriteBatch.setColor(1, 1, 1, opacity);
        spriteBatch.draw(bufferTexture, 0, 0, width, height);
        spriteBatch.end();
        fadedElapsed += delta;

        // We might have finished fading if the opacity is full
        if (opacity == 1 && !fadingOut) {
//            game.setScreen(toScreen);
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        if (frameBuffer != null)
            frameBuffer.dispose();

        frameBuffer = new FrameBuffer(Pixmap.Format.RGB565, width, height, false);
        bufferTexture = new TextureRegion(frameBuffer.getColorBufferTexture());
        bufferTexture.flip(false, true);
    }

    //endregion

    //region Disposing

    @Override
    public void dispose() {
        frameBuffer.dispose();
        if (disposeAfter)
            fromScreen.dispose();
    }

    //endregion

    //region Unused methods

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
