package com.libGdx.test;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/29 10:17
 */
public class TuoWeiTest extends LibGdxTestMain {
    public static void main(String[] args) {
        TuoWeiTest test = new TuoWeiTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        PictureTrail2 pictureTrail = new PictureTrail2();
        pictureTrail.setRegion(new TextureRegion(Asset.getAsset().getTexture("Tailing.png")));
        stage.addActor(pictureTrail);
        pictureTrail.toFront();
        pictureTrail.setPosition(300,300);
        pictureTrail.addAction(Actions.moveBy(500,1000,2f));
//        BezierMoveAction action = new BezierMoveAction();
//        action.setPictureTrail(pictureTrail);
//        action.setBezier(100, 100,100,100, 300,100 ,800,900);
//        action.setInterpolation(new BseInterpolation(0.25f, 0, 0.75f, 1));
//        action.setDuration(6);
//        Actor actor= new Actor();
//        addActor(actor);
//        actor.addAction(action);
    }
}
