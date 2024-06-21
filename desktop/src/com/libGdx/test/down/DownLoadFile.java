package com.libGdx.test.down;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.npath.NinePathNew;

public class DownLoadFile extends LibGdxTestMain {
    public static void main(String[] args) {
        DownLoadFile downLoadFile = new DownLoadFile();
        downLoadFile.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Texture texture = Asset.getAsset().getTexture("000.png");
        TextureRegion region = new TextureRegion(texture);
        NinePathNew ninePatch = new NinePathNew(region,100,100,100,100);
        Image image = new Image(ninePatch);

        addActor(image);
    }
}
