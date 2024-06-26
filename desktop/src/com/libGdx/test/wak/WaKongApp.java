package com.libGdx.test.wak;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class WaKongApp extends LibGdxTestMain {
    public static void main(String[] args) {
        WaKongApp app = new WaKongApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        WakongGroup wakongGroup = new WakongGroup();
        addActor(wakongGroup);
    }
}
