package com.libGdx.test.vertices;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        VertextGroup vertextGroup = new VertextGroup();
        addActor(vertextGroup);
    }
}
