package com.libGdx.test.alpha;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class ChannelApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ChannelApp alphaTestApp = new ChannelApp();
        alphaTestApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        TextureRegion region1 = new TextureRegion(Asset.getAsset().getTexture("assets/3_34_24.png"));
        TextureRegion region2 = new TextureRegion(Asset.getAsset().getTexture("assets/ui_hole.png"));
//        ClipActor clipActor = new ClipActor(region2,region1);
//        addActor(clipActor);
//        ChannelTest alphaTest = new ChannelTest();
//        addActor(alphaTest);
        AlphaTest alphaTest = new AlphaTest();
        addActor(alphaTest);
    }
}
