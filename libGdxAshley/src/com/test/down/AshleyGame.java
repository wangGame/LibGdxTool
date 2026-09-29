package com.test.down;

import com.kw.gdx.BaseGame;
import com.test.down.screen.LoadingScreen;

public class AshleyGame extends BaseGame {
    @Override
    public void create() {
        super.create();
    }

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new LoadingScreen(this));
    }
}
