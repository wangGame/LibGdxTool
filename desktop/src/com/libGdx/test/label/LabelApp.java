package com.libGdx.test.label;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class LabelApp extends LibGdxTestMain {
    public static void main(String[] args) {
        LabelApp app = new LabelApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        LabelDemo labelDemo = new LabelDemo();
        addActor(labelDemo);
    }
}
