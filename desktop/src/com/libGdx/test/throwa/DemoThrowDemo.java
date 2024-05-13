package com.libGdx.test.throwa;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.crash.CrashUtils;
import com.libGdx.test.base.LibGdxTestMain;

public class DemoThrowDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        DemoThrowDemo throwDemo = new DemoThrowDemo();
        throwDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        int  dd = 1/0;
    }
}
