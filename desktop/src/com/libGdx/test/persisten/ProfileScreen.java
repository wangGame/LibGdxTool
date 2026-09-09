package com.libGdx.test.persisten;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;

public class ProfileScreen extends BaseScreen {
    public ProfileScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        Label label = new Label("Profile Screen", new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Cali_75.fnt");
        }});
        addActor(label);
        label.setY(1920 + offsetTop, Align.top);
    }
}
