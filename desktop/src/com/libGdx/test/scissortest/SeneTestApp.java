package com.libGdx.test.scissortest;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class SeneTestApp extends LibGdxTestMain {
    public static void main(String[] args) {
        SeneTestApp seneTestApp = new SeneTestApp();
        seneTestApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Image image = new Image(Asset.getAsset().getTexture("assets/000.png"));
        addActor(image);

        SeneTest seneTest = new SeneTest();
        addActor(seneTest);
    }
}
