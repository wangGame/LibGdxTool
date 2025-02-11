package com.libGdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

import java.io.File;

public class Deal extends LibGdxTestMain {
    public static void main(String[] args) {
        Deal deal = new Deal();
        deal.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        FileHandle internal = Gdx.files.internal("hh");
        for (FileHandle fileHandle : internal.list()) {
            dealPicture(fileHandle.file());
        }
    }

    public void dealPicture(File a) {
        Pixmap pixmap = new Pixmap(new FileHandle(a));
        pixmap.setBlending(Pixmap.Blending.None);

        for (int i = 0; i < pixmap.getWidth(); ++i) {
            for (int j = 0; j < pixmap.getHeight(); ++j) {
                Color color = new Color(pixmap.getPixel(i, j));
                if (color.a == 0) {
                    color.r = 251/255.0F;
                    color.g = 227/255.0F;
                    color.b = 196/255.0F;
                    color.a = 0f;
//                    color.r = 1F;
//                    color.g = 1F;
//                    color.b = 1F;
//                    color.a = 0f;
                }
                pixmap.drawPixel(i, j, Color.rgba8888(color));
            }
        }
        pixmap.setBlending(Pixmap.Blending.None);
        PixmapIO.writePNG(new FileHandle("hh/out.png"), pixmap);
        pixmap.dispose();
    }

}
