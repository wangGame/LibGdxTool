package com.joker.domos;

import com.joker.domos.screen.LoadScreen;
import com.kw.gdx.BaseGame;

public class GameTest extends BaseGame {

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new LoadScreen(this));
    }
}
