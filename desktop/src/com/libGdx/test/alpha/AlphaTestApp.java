package com.libGdx.test.alpha;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.clip.ClipActor;
import com.libGdx.test.base.LibGdxTestMain;

public class AlphaTestApp extends LibGdxTestMain {
    public static void main(String[] args) {
        AlphaTestApp alphaTestApp = new AlphaTestApp();
        alphaTestApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        AlphaTest test = new AlphaTest();
//        addActor(test);
        TextureRegion region1 = new TextureRegion(Asset.getAsset().getTexture("assets/3_34_24.png"));
        TextureRegion region2 = new TextureRegion(Asset.getAsset().getTexture("assets/ui_hole.png"));
        ClipActor clipActor = new ClipActor(region2,region1);
        addActor(clipActor);

    }
}
