package com.libGdx.test.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

@GameInfo
public class ViewTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ViewTest viewTest = new ViewTest();
        viewTest.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        setScreen(TestScreen.class);
    }
}
