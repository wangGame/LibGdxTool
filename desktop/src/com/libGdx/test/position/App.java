package com.libGdx.test.position;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image ps = new Image(Asset.getAsset().getTexture("assets/0_1_41_512.jpg"));
        addActor(ps);

        ps.setRotation(10);
        System.out.println(ps.getX());


        System.out.println(ps.getX(Align.right));

    }
}
