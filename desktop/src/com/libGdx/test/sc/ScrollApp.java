package com.libGdx.test.sc;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class ScrollApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ScrollApp scrollApp=  new ScrollApp();
        scrollApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        SeneTest seneTest = new SeneTest();
        addActor(seneTest);
    }
}
