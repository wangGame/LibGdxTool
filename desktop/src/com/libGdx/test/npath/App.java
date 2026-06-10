package com.libGdx.test.npath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.npath.GridPatch;
import com.kw.gdx.npath.GridPatchDrawable;
import com.libGdx.test.base.LibGdxTestMain;


public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Texture texture = Asset.getAsset().getTexture("assets/bg_2.png");

        int w = texture.getWidth();
        int h = texture.getHeight();

        int glowLeft = 12;
        int borderLeft = 8;
        int borderRight = 8;
        int glowRight = 12;

        int glowTop = 12;
        int borderTop = 8;
        int borderBottom = 8;
        int glowBottom = 12;

        int centerW = w - glowLeft - borderLeft - borderRight - glowRight;
        int centerH = h - glowTop - borderTop - borderBottom - glowBottom;

        GridPatch patch = new GridPatch(
                texture,

                new int[]{
                        glowLeft,
                        borderLeft,
                        centerW,
                        borderRight,
                        glowRight
                },

                new int[]{
                        glowTop,
                        borderTop,
                        centerH,
                        borderBottom,
                        glowBottom
                },

                new boolean[]{
                        false, false, true, false, false
                },

                new boolean[]{
                        false, false, true, false, false
                }
        );
        Image image = new Image(new GridPatchDrawable(patch));
        addActor(image);
    }
}
