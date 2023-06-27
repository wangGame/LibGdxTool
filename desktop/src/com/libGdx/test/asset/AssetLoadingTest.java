package com.libGdx.test.asset;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/26 19:22
 */
public class AssetLoadingTest extends LibGdxTestMain {
    public static void main(String[] args) {
        AssetLoadingTest test  = new AssetLoadingTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        {
            SpineResResourceTest test = SpineResResourceTest.getInstance();
            test.loadRes();
            Asset.assetManager.finishLoading();
            test.getRes();
            if (Asset.assetManager.isLoaded(test.jiazaiTupic)) {
                System.out.println("-");
            }
        }
        {
            EffectResResourceTest test = EffectResResourceTest.getInstance();
            test.loadRes();
            Asset.assetManager.finishLoading();
            test.getRes();
            if (Asset.assetManager.isLoaded(test.jiazaiTupic)) {
                System.out.println("-");
            }
        }
    }
}
