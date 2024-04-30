package com.libGdx.test.anr;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.anr.ANRDEMO;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

@GameInfo(width = 100)
@ANRDEMO(delaytime = 500)
public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);


        while (true) {

        }
    }
}
