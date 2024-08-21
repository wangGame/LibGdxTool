package com.libGdx.test.trile;

import com.kw.gdx.BaseGame;

public class TestGame extends BaseGame {

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new LoadingScreen(TestGame.this));
    }
}
