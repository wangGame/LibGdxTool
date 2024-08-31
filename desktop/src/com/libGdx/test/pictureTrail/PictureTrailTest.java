package com.libGdx.test.pictureTrail;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.trail.BezierMoveAction;
import com.kw.gdx.trail.PictureTrail;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/25 19:22
 */
public class PictureTrailTest extends LibGdxTestMain {
    public static void main(String[] args) {
        PictureTrailTest test = new PictureTrailTest();
        test.start(test);


    }
    PictureTrail pictureTrail;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        pictureTrail = new PictureTrail();
        pictureTrail.setRegion(new TextureRegion(
                Asset.getAsset().getTexture("assets/texture.png")));
        stage.addActor(pictureTrail);


        BezierMoveAction moveAction = new BezierMoveAction();
        moveAction.setPictureTrail(pictureTrail);
        moveAction.setBezier(0,0,100,100,900,500,0,0);
        moveAction.setDelayTime(1);
        moveAction.setDuration(10);
        stage.addAction(moveAction);

        stage.addListener(new ClickListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);

                if (pictureTrail != null) {
                    pictureTrail.x = y;
                    pictureTrail.y = x;
                }
                pictureTrail.setPosition(x, y, Align.center);

            }
        });
    }
}
