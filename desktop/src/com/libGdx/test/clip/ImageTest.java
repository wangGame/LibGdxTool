package com.libGdx.test.clip;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class ImageTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ImageTest test = new ImageTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ImageXT imageXT = new ImageXT();
        addActor(imageXT);
    }
}
