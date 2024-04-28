package com.libGdx.test.pengzhuang;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

import kw.dgx.aabb.BroadImage;

public class BroadApp extends LibGdxTestMain {
    public static void main(String[] args) {
        BroadApp app = new BroadApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        BroadImage broadImage = new BroadImage();
        addActor(broadImage);

        BroadImage broadImage1 = new BroadImage();
        addActor(broadImage1);
    }
}
