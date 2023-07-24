package com.libGdx.test.mesh;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.loader.SpineActor;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.spine.SpineTest;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/4 14:00
 */
public class Demo01 extends LibGdxTestMain {
    public static void main(String[] args) {
        Demo01 test = new Demo01();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

    }
}
