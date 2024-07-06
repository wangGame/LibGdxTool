package com.libGdx.test.process;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class ProcessApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ProcessApp app = new ProcessApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ProcessGroup processGroup = new ProcessGroup();
        addActor(processGroup);
    }
}
