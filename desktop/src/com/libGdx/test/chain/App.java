package com.libGdx.test.chain;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.task.TaskOne;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        TaskOne taskOne = new TaskOne();
    }
}
