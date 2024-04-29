package com.libGdx.test.npath;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

import kw.dgx.npath.NPath;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        NPath nPath = new NPath(3);
        stage.addActor(nPath);
    }
}
