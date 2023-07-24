package com.libGdx.test.poly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ShortArray;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/24 13:56
 */
@GameInfo(width = 320,height = 400,batch = Constant.COUPOLYGONBATCH)
public class PolygonTest extends LibGdxTestMain {
    public static void main(String[] args) {
        PolygonTest polygonTest = new PolygonTest();
        polygonTest.start(polygonTest);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        stage.addActor(new PolyActor());
    }
}
