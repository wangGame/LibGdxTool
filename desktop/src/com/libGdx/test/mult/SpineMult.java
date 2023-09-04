package com.libGdx.test.mult;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.loader.SpineActor;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/8/25 18:39
 */
public class SpineMult extends LibGdxTestMain {
    public static void main(String[] args) {
        SpineMult mult = new SpineMult();
        mult.start(mult);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        SpineActor actor = new SpineActor("assets/3_34_24");
        stage.addActor(actor);
        actor.setPosition(400,600);
        actor.setAnimation("animation",true);
    }
}
