package com.libGdx.test.cut;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.spine.SpineTest;

/**
 * @Auther jian xian si qi
 * @Date 2023/10/25 18:51
 */
public class CutTest extends LibGdxTestMain {
    public static void main(String[] args) {
        CutTest test = new CutTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        SpineActor spineActor = new SpineActor("assets/spine/idle");
        addActor(spineActor);
        spineActor.setAnimation("1_2",true);
        spineActor.setPosition(300,0);
    }
}