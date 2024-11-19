package com.libGdx.test.action;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.besier.BseInterpolation;
import com.libGdx.test.base.LibGdxTestMain;

public class TestBse extends LibGdxTestMain {
    public static void main(String[] args) {
        TestBse testBse = new TestBse();
        testBse.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        SpineActor actorSpine = new SpineActor("assets/spine/quxian_ck");
        addActor(actorSpine);
        actorSpine.setPosition(500,500);
        actorSpine.addAction(Actions.delay(2,Actions.run(()->{

            Image img = new Image(Asset.getAsset().getTexture("assets/spine/A.png"));
            addActor(img);
            img.setPosition(100,500);
            img.addAction(
                    Actions.moveTo(100f,500+283.86f,1.0f, new BseInterpolation(
                            0.25f,0,0.75f,1.0f
                    ))
            );
            actorSpine.setAnimation("1",false);
        })));
    }
}
