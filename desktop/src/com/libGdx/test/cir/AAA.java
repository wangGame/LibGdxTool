package com.libGdx.test.cir;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/11/26 11:26
 */
class AAA extends LibGdxTestMain {
    public static void main(String[] args) {
        AAA aaa = new AAA();
        aaa.start(aaa);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        MaskTest test = new MaskTest();
        stage.addActor(test);
    }
}
