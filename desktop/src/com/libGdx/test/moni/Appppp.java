package com.libGdx.test.moni;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class Appppp extends LibGdxTestMain {
    public static void main(String[] args) {
        Appppp appppp = new Appppp();
        appppp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image i = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        addActor(i);
    }
}
