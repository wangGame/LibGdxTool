package com.tony.puzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;

public class LoadingScreen extends BaseScreen {
    public LoadingScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        addActor(image);
        image.setY(Constant.GAMEHIGHT - Configuration.top, Align.top);
        image.setOrigin(Align.topLeft);
        image.setScale(1.4f);
    }
}
