package com.libGdx.test;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class AppC extends LibGdxTestMain {
    public static void main(String[] args) {
        AppC appC = new AppC();
        appC.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image image = new Image(Asset.getAsset().getTexture("assets/board1.png"));
        addActor(image);

        Image image1 = new Image(Asset.getAsset().getTexture("assets/board2.png"));
        addActor(image1);
        image1.setY(600);
        //844  1500

    }
}
