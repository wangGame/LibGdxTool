package com.libGdx.test.terrin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class TerrinApp extends LibGdxTestMain {
    public static void main(String[] args) {
        TerrinApp app = new TerrinApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Texture texture = Asset.getAsset().getTexture("assets/000.png");
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        Color sample = new Color();
        int tw = pixmap.getWidth();
        int th = pixmap.getHeight();
        final int MAP_SIZE = 1000;
        float [][]heightMap = new float[MAP_SIZE][MAP_SIZE];
        final float AMPLITUDE  = 20f;
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                int tx = (x * tw)/MAP_SIZE;
                int ty = (y * th)/MAP_SIZE;
                int rgba = pixmap.getPixel(ty,tx);
                sample.set(rgba);
                heightMap[y][x] = AMPLITUDE*(sample.r -0.5f );
            }
        }
        texture.getTextureData().disposePixmap();
        System.out.println("------------------------");
    }
}
