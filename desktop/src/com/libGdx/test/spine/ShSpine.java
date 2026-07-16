package com.libGdx.test.spine;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

@GameInfo(width = 2080, height = 2920,viewportType = Constant.EXTENDVIEWPORT ,batch = Constant.COUPOLYGONBATCH)
public class ShSpine extends LibGdxTestMain {
    public static void main(String[] args) {
        ShSpine shSpine = new ShSpine();
        shSpine.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        SpineActor actor = new SpineActor("assets/fan");
        addActor(actor);
        actor.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f, Align.center);
        for (Animation animation : actor.getAnimaState().getData().getSkeletonData().getAnimations()) {
            System.out.println(animation.getName());
        }
        actor.setAnimation("animation4",true);
    }
}
