package com.libGdx.tools.unplist.dealpic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.File;

public class DealPic extends Game {
    private static final String rootPath = "Asset" + File.separator + "deal";
    String desPath = "Asset" + File.separator + "NewPicture";

    @Override
    public void create() {
        String srcPath = rootPath;
        File readPictures = new File(srcPath);
        for (File a : readPictures.listFiles()) {
            if (a.getName().endsWith(".png")) {
                dealPicture(a,0);
            }
        }
    }

    public void dealPicture(File a,int in) {
        Pixmap pixmap = new Pixmap(new FileHandle(a));
        pixmap.setBlending(Pixmap.Blending.None);
        for (int i = 0; i < pixmap.getWidth(); ++i) {
            for (int j = 0; j < pixmap.getHeight(); ++j) {
                Color color = new Color(pixmap.getPixel(i, j));
                if (color.a == 0) {
                    color.r = 255/255.0F;
                    color.g = 255/255.0F;
                    color.b = 255/255.0F;
                    color.a = 0f;
                }
                pixmap.drawPixel(i, j, Color.rgba8888(color));
            }
        }
        pixmap.setBlending(Pixmap.Blending.None);
        PixmapIO.writePNG(new FileHandle(desPath + File.separator + in+File.separator+a.getName()), pixmap);
        pixmap.dispose();
    }

    public Color searchColor(Pixmap pixmap, int i, int j) {
        Color c = new Color(pixmap.getPixel(i, j + 1));
        if (c.a == 1) {
            return c;
        }
        c = new Color(pixmap.getPixel(i, j - 1));
        if (c.a == 1) {
            return c;
        }
        c = new Color(pixmap.getPixel(i + 1, j));
        if (c.a == 1) {
            return c;
        }
        c = new Color(pixmap.getPixel(i - 1, j));
        if (c.a == 1) {
            return c;
        }
        c = new Color(pixmap.getPixel(i + 1, j + 1));
        if (c.a == 1) {
            return c;
        }
        c = new Color(pixmap.getPixel(i - 1, j + 1));
        if (c.a == 1) {
            return c;
        }
        c = new Color(pixmap.getPixel(i + 1, j - 1));
        if (c.a == 1) {
            return c;
        }
        c = new Color(pixmap.getPixel(i - 1, j - 1));
        if (c.a == 1) {
            return c;
        }
        return Color.WHITE;
    }

    public static void main(String[] strings) {
        new LwjglApplication(new DealPic());
    }
}