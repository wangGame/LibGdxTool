package com.tony.dominoes.screen;

import com.kw.gdx.BaseGame;
import com.kw.gdx.screen.BaseScreen;
public class LoadingScreen extends BaseScreen {

    public LoadingScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        setScreen(AnimationToolScreen.class);
    }
}
