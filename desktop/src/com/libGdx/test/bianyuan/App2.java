package com.libGdx.test.bianyuan;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class App2 extends LibGdxTestMain {

    public static void main(String[] args) {
        App2 app = new App2();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        LunKuoGroup group = new LunKuoGroup();
        addActor(group);
    }
}
