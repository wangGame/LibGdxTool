package com.libGdx.test.colorcircle;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        Constant.viewColor.set(1,1,1,1);
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        ColorCircle colorCircle = new ColorCircle(1);
//        addActor(colorCircle);
        ConnectionCircle connectionCircle = new ConnectionCircle();
        addActor(connectionCircle);
        connectionCircle.setScale(1);
        connectionCircle.setDebug(true);
        connectionCircle.setPosition(100,100);
    }
}
