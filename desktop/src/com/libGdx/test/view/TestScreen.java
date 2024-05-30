package com.libGdx.test.view;

import com.kw.gdx.BaseGame;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.screen.BaseScreen;

@ScreenResource("cocos/gameview.json")
public class TestScreen extends BaseScreen {

    public TestScreen(BaseGame game) {
        super(game);
    }
}
