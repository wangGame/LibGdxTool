package com.libGdx.test.tietu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class TieApp extends LibGdxTestMain {
    public static void main(String[] args) {
        TieApp app = new TieApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Constant.viewColor.set(Color.BROWN);


        BodyImage bodyImage = new BodyImage();
        addActor(bodyImage);
        bodyImage.setPosition(Constant.GAMEWIDTH/2.0f,Constant.GAMEHIGHT/2.0f,Align.center);
        bodyImage.setScale(6);


//        Texture model = Asset.getAsset().getTexture("0_1_41_512.jpg");
//        Image image = new Image(model);
//        addActor(image);
    }
}
