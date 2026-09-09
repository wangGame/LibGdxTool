package com.libGdx.test.persisten;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;

public class ShowMain2Screen extends BaseScreen {
    public ShowMain2Screen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        Image image = new Image(Asset.getAsset().getTexture("shuoming.png"));
        image.setPosition(100, 100);
        stage.addActor(image);
    }
}
