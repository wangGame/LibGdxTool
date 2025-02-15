package com.libGdx.test.bianyuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

import java.util.ArrayList;
import java.util.List;

public class App2 extends LibGdxTestMain {

    public static void main(String[] args) {
        App2 app = new App2();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        LunKuoGroup group = new LunKuoGroup();
        addActor(group);
    }
}
