package com.tony.puzzle;

import android.app.Activity;

import com.kw.gdx.BaseGame;

public class TestGame extends BaseGame {

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new LoadingScreen(TestGame.this));
    }
}
