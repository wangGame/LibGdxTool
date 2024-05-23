package com.libGdx.test.boxtest;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class Box2DTest extends LibGdxTestMain {
    public static void main(String[] args) {
        Box2DTest test = new Box2DTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        BlackGroup group = new BlackGroup();
        addActor(group);
        group.setPosition(300,500);
    }
}
