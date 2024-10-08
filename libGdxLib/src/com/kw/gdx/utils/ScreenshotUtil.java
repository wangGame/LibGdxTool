package com.kw.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

public class ScreenshotUtil {
    private static int counter = 1;

    private ScreenshotUtil() {
    }

    public static void saveScreenshot() {
        while(true) {
            try {
                FileHandle fh = new FileHandle("screenshot" + counter++ + ".png");
                if (fh.exists()) {
                    continue;
                }
                Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                PixmapIO.writePNG(fh, pixmap);
                pixmap.dispose();
            } catch (Exception var2) {
            }

            return;
        }
    }

    private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown) {
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);
        if (yDown) {
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;

            for(int i = 0; i < h; ++i) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }

            pixels.clear();
            pixels.put(lines);
        }

        return pixmap;
    }
}