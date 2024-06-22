package com.libGdx.test.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.BaseGame;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.screen.BaseScreen;

@ScreenResource("cocos/gameview.json")
public class TestScreen extends BaseScreen {

    public TestScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();


        int screenX = game.getStageViewport().getScreenX();
        Actor levelPanel = rootView.findActor("levelPanel");
        levelPanel.setX(levelPanel.getX()-screenX*5);
    }
}
