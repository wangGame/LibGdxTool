package com.libGdx.test.ai.regression;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App a = new App();
        a.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        UserGroup userGroup = new UserGroup();
        addActor(userGroup);
    }
}
