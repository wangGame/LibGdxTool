package com.libGdx.test.stencil;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/26 7:25
 */
class Test extends LibGdxTestMain {

    public static void main(String[] args) {
        new Test().start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        SeneTest seneTest = new SeneTest();
        addActor(seneTest);
    }
}
