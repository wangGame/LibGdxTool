package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.clip.ClipActor;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/25 19:00
 */
public class ClipTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ClipTest test = new ClipTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        TextureRegion region = new TextureRegion(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        ClipActor actor = new ClipActor(region,false,20,20);
        stage.addActor(actor);
        actor.setSpeed(0.1f);
        actor.setProgress(0.5f);
    }
}
