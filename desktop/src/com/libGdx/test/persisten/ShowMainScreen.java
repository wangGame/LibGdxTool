package com.libGdx.test.persisten;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;

public class ShowMainScreen extends BaseScreen {
    public ShowMainScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        image.setPosition(100, 100);
        stage.addActor(image);
        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setScreen(ShowMain2Screen.class);
            }
        });
    }
}
