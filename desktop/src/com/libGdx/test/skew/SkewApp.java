package com.libGdx.test.skew;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class SkewApp extends LibGdxTestMain {
    public static void main(String[] args) {
        SkewApp skewApp = new SkewApp();
        skewApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        SkewGroup skewGroup = new SkewGroup();
        addActor(skewGroup);

        Image image = new Image(Asset.getAsset().getTexture("assets/000.png"));
        skewGroup.addActor(image);
        image.setSize(500,500);
        skewGroup.setSkew(0,40);
    }
}
