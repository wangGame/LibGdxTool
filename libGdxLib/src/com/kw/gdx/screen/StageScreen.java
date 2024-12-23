package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class StageScreen extends ScreenAdapter {
    private final Stage mStage;
    private final Viewport mViewport;
    private final InputMultiplexer mInputMultiplexer;

    public StageScreen(Viewport viewport) {
        mViewport = viewport;
        mStage = new Stage(mViewport);
        mInputMultiplexer = new InputMultiplexer();
        mInputMultiplexer.addProcessor(mStage);
    }

    public void prependInputProcessor(InputProcessor inputProcessor) {
        mInputMultiplexer.addProcessor(0, inputProcessor);
    }

    public Stage getStage() {
        return mStage;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        mStage.act(delta);
        if (isBackKeyPressed()) {
            onBackPressed();
        }
        mStage.draw();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(mInputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        mViewport.update(width, height, true);
    }

    public abstract void onBackPressed();

    public abstract boolean isBackKeyPressed();
}
