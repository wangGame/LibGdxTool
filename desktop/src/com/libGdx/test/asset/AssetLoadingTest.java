package com.libGdx.test.asset;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/26 19:22
 *
 *
 */
public class AssetLoadingTest extends LibGdxTestMain {
    public static void main(String[] args) {
        AssetLoadingTest test  = new AssetLoadingTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        {
//            SpineResResourceTest test = SpineResResourceTest.getInstance();
//            test.loadRes();
//            Asset.getAsset().getAssetManager().finishLoading();
//            test.getRes();
//            if (Asset.getAsset().getAssetManager().isLoaded(test.jiazaiTupic)) {
//                System.out.println("-");
//            }
//        }
//        {
//            EffectResResourceTest test = EffectResResourceTest.getInstance();
//            test.loadRes();
//            Asset.getAsset().getAssetManager().finishLoading();
//            test.getRes();
//            if (Asset.getAsset().getAssetManager().isLoaded(test.jiazaiTupic)) {
//                System.out.println("-");
//            }
//        }
        TextureAtlas atlas = Asset.getAsset().getAtlas("spine/maoni.atlas");

        SpineActor spineActor = new SpineActor("spine/maoni");
//
//        maomi_fuor.skel
//        maoni.atlas
//        maoni.png

    }
}
