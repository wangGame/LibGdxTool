package com.libGdx.test.path;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class PathApp extends LibGdxTestMain {
    public static void main(String[] args) {
        PathApp pathApp = new PathApp();
        pathApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        PathGroup pathGroup = new PathGroup();
        addActor(pathGroup);
    }
}
