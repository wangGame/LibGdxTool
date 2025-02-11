package com.libGdx.test.ball;

import com.badlogic.gdx.scenes.scene2d.Stage;
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
        BallImage ballImage = new BallImage(Asset.getAsset().getTexture("ball/0.png"));
        addActor(ballImage);
    }
}
