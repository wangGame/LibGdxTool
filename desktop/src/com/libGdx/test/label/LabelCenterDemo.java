package com.libGdx.test.label;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.utils.LabelCenter;
import com.libGdx.test.base.LibGdxTestMain;

public class LabelCenterDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        LabelCenterDemo center = new LabelCenterDemo();
        center.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        LabelCenter labelCenter = new LabelCenter();
    }
}
