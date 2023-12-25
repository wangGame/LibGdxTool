package com.libGdx.test.pictureTrail;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.picturetail.PictureTrail;
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
                Asset.getAsset().getTexture("fangshier.png")));
        stage.addActor(pictureTrail);
        pictureTrail.toFront();

        stage.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                pictureTrail.x = x;
                pictureTrail.y = y;
//                System.out.println(x +"  ------ "+y);
                pictureTrail.setPosition(x,y);

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });
//        BezierMoveAction action = new BezierMoveAction();
//        action.setDuration(10.7F);
//        action.setPictureTrail(pictureTrail);
//        action.setBezier(
//                0, 0,
//                1200, 1200,
//                1400, 2400,
//                200, 200);
//        Image image = new Image(Asset.getAsset().getTexture("fangshier.png"));
//        stage.addActor(image);
//        image.addAction(action);
    }
}
