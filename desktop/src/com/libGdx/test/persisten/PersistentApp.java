package com.libGdx.test.persisten;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class PersistentApp extends LibGdxTestMain {
    public static void main(String[] args) {
        PersistentApp app = new PersistentApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        setScreen(ShowMainScreen.class);
    }
}
