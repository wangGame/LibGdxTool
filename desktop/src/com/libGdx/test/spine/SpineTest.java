package com.libGdx.test.spine;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.loader.SpineActor;
import com.libGdx.test.base.LibGdxTestBase;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.effect.EffectTest;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/25 12:57
 */
public class SpineTest extends LibGdxTestMain {
    public static void main(String[] args) {
        SpineTest test = new SpineTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        SpineActor spineActor = new SpineActor("dog_xuanguan");
//        stage.addActor(spineActor);
//        spineActor.setAnimation("animation", true);
//        spineActor.setPosition(400, 400);
        {
            SpineActor spineActor = new SpineActor("spine/0_loading");
            stage.addActor(spineActor);
            spineActor.setAnimation("animation", true);
            spineActor.setPosition(400, 400);
        }
        {
            SpineActor spineActor = new SpineActor("spine/0_loading");
            stage.addActor(spineActor);
            spineActor.setAnimation("animation", true);
            spineActor.setPosition(400, 800);

            spineActor.setClip(true);
            //中心便宜距离
            spineActor.setBeginX(-140);
            spineActor.setBeginY(-140);
            //裁剪的宽高
            spineActor.setW(280);
            spineActor.setH(280);
        }
    }
}
