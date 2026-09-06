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
//        TextureRegion region = new TextureRegion(Asset.getAsset().getTexture("7.png"));
//        TextureRegion region1 = new TextureRegion(Asset.getAsset().getTexture("3_34_24.png"));
//        ClipActor actor = new ClipActor(region,region1);
//        stage.addActor(actor);

//        ClipDemo clipDemo = new ClipDemo();
//        addActor(clipDemo);


        ClipActor clipActor = new ClipActor(
                new TextureRegion(new Texture("assets/ad_progress.png")),
                new TextureRegion(new Texture("assets/board1.png")));
        clipActor.setPosition(100, 100);
        stage.addActor(clipActor);
    }
}
