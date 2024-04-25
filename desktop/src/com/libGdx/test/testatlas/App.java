package com.libGdx.test.testatlas;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        TextureAtlas atlas = Asset.getAsset().getAtlas("assets/out.atlas");
        TextureAtlas.AtlasRegion bg_theme_city = atlas.findRegion("bg_theme_city_1");
        Image image = new Image(bg_theme_city);
        image.setOrigin(Align.center);
        image.setRotation(90);
        addActor(image);
    }
}
