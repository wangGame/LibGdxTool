package com.libGdx.test.qx;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

import java.util.ArrayList;

public class QuxianApp extends LibGdxTestMain {
    public static void main(String[] args) {
        QuxianApp app = new QuxianApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Vector2 startV2 = new Vector2(0,0);
        Vector2 middelV2 = new Vector2(100,100);

        for (int i = 0; i < 15; i++) {
            Image image = new Image(Asset.getAsset().getTexture("assets/7.png"));
            addActor(image);
            image.setPosition(middelV2.x, middelV2.y);
            middelV2.x += 80;
            middelV2.y += 50 - 1*i*i;
        }

    }
}
