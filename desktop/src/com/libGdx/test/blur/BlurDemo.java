package com.libGdx.test.blur;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.beser.B;

public class BlurDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        BlurDemo blurDemo = new BlurDemo();
        blurDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        BlurGroup blurGroup = new BlurGroup();
        addActor(blurGroup);
    }
}
