package com.libGdx.test.pixmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class CirPixmap extends LibGdxTestMain {
    public static void main(String[] args) {
        CirPixmap cirPixmap = new CirPixmap();
        cirPixmap.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        splitAndSaveRounded();
    }

    public void splitAndSaveRounded() {

        Pixmap big = new Pixmap(Gdx.files.internal("0_1_41_512.jpg"));

        int tileWidth = 256;
        int tileHeight = 256;
        int radius = 10;   // 圆角半径

        int cols = 4;
        int rows = 4;

        tileWidth = big.getWidth() / cols;
        tileHeight = big.getHeight()/rows;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                Pixmap small = new Pixmap(tileWidth, tileHeight, Pixmap.Format.RGBA8888);

                // copy tile
                small.drawPixmap(
                        big,
                        0, 0,
                        x * tileWidth, y * tileHeight,
                        tileWidth, tileHeight
                );

                applyRoundedMask(small, radius);

                FileHandle file = Gdx.files.local("out/tile_" + x + "_" + y + "_rounded.png");
                PixmapIO.writePNG(file, small);

                small.dispose();
            }
        }

        big.dispose();
    }

    private void applyRoundedMask(Pixmap pixmap, int r) {
        int w = pixmap.getWidth();
        int h = pixmap.getHeight();

        pixmap.setBlending(Pixmap.Blending.None);   // 直接覆盖像素
        int clearColor = 0;                         // RGBA = 0 透明

        // 左上角
        for (int y = 0; y < r; y++) {
            for (int x = 0; x < r; x++) {
                int dx = r - x;
                int dy = r - y;
                if (dx * dx + dy * dy > r * r) {
                    pixmap.drawPixel(x, y, clearColor);
                }
            }
        }

        // 右上角
        for (int y = 0; y < r; y++) {
            for (int x = w - r; x < w; x++) {
                int dx = x - (w - r - 1);
                int dy = r - y;
                if (dx * dx + dy * dy > r * r) {
                    pixmap.drawPixel(x, y, clearColor);
                }
            }
        }

        // 左下角
        for (int y = h - r; y < h; y++) {
            for (int x = 0; x < r; x++) {
                int dx = r - x;
                int dy = y - (h - r - 1);
                if (dx * dx + dy * dy > r * r) {
                    pixmap.drawPixel(x, y, clearColor);
                }
            }
        }

        // 右下角
        for (int y = h - r; y < h; y++) {
            for (int x = w - r; x < w; x++) {
                int dx = x - (w - r - 1);
                int dy = y - (h - r - 1);
                if (dx * dx + dy * dy > r * r) {
                    pixmap.drawPixel(x, y, clearColor);
                }
            }
        }
    }

}
