package com.libGdx.test.pictureTrail;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.asset.Asset;
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
                pictureTrail.setPosition(x,y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}
