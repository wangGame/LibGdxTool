package com.joker.domos.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;

public class LoadScreen extends BaseScreen {
    public LoadScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
//        TextField textField = new TextField("",new TextField.TextFieldStyle(){{
//            font = Asset.getAsset().loadBitFont("font/Manrope-Bold_56_1.fnt");
//            cursor = new TextureRegionDrawable(Asset.getAsset().getSprite("textfield/textc.png"));
//            fontColor = Color.WHITE;
//        }});
//        textField.setSize(Constant.GAMEWIDTH,200);
//        addActor(textField);
//        textField.setDebug(true);
//        textField.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT*3/5, Align.center);
        Label label = new Label("Loading...",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Manrope-Bold_56_1.fnt");
            fontColor = Color.WHITE;
        }});
        addActor(label);
        label.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT*3/5, Align.center);
        label.setText("new player ");
        label.setDebug(true);
        label.setAlignment(Align.center);


    }
}
