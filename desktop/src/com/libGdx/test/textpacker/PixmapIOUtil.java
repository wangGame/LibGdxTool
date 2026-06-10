package com.libGdx.test.textpacker;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

public class PixmapIOUtil {
    public static void writePng(FileHandle outputFile, Pixmap pixmap) {
        PixmapIO.writePNG(outputFile, pixmap);
    }
}