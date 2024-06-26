package com.libGdx.test.scissortest;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class SeneTestApp extends LibGdxTestMain {
    public static void main(String[] args) {
        SeneTestApp seneTestApp = new SeneTestApp();
        seneTestApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        SeneTest seneTest = new SeneTest();
        addActor(seneTest);
    }
}
