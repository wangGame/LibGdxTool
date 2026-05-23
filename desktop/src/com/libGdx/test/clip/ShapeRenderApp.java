package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.polyGroup.PolygonClipGroup;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class ShapeRenderApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ShapeRenderApp app = new ShapeRenderApp();
        app.start();
    }



    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        PolygonClipGroup clipGroup = new PolygonClipGroup(new ShapeRenderer());
        clipGroup.setSize(500,500);
        addActor(clipGroup);
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        clipGroup.addActor(image);

    }
}
