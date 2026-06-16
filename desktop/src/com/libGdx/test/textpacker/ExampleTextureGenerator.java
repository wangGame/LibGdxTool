package com.libGdx.test.textpacker;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class ExampleTextureGenerator extends LibGdxTestMain {

    public static void main(String[] args) {
        ExampleTextureGenerator exampleTextureGenerator = new ExampleTextureGenerator();
        exampleTextureGenerator.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        FileHandle inputDir = Gdx.files.absolute("input");
        inputDir.mkdirs();

        generateAo(inputDir.child("ao.png"), 256, 256);
        generateRoughness(inputDir.child("roughness.png"), 256, 256);
        generateMetallic(inputDir.child("metallic.png"), 256, 256);
        generateHeight(inputDir.child("height.png"), 256, 256);

        System.out.println("Generated example textures in ./input");
    }

    private static void generateAo(FileHandle file, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        try {
            float cx = width / 2f;
            float cy = height / 2f;
            float maxDist = (float)Math.sqrt(cx * cx + cy * cy);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float dx = x - cx;
                    float dy = y - cy;
                    float dist = (float)Math.sqrt(dx * dx + dy * dy);
                    float t = 1f - MathUtils.clamp(dist / maxDist, 0f, 1f);

                    int v = MathUtils.clamp(Math.round(t * 255f), 0, 255);
                    int rgba = ((v & 0xff) << 24)
                            | ((v & 0xff) << 16)
                            | ((v & 0xff) << 8)
                            | 255;
                    pixmap.drawPixel(x, y, rgba);
                }
            }

            PixmapIOUtil.writePng(file, pixmap);
        } finally {
            pixmap.dispose();
        }
    }

    private static void generateRoughness(FileHandle file, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        try {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float t = x / (float)(width - 1);
                    int v = MathUtils.clamp(Math.round(t * 255f), 0, 255);

                    int rgba = ((v & 0xff) << 24)
                            | ((v & 0xff) << 16)
                            | ((v & 0xff) << 8)
                            | 255;
                    pixmap.drawPixel(x, y, rgba);
                }
            }

            PixmapIOUtil.writePng(file, pixmap);
        } finally {
            pixmap.dispose();
        }
    }

    private static void generateMetallic(FileHandle file, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        try {
            int cell = 32;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    boolean even = ((x / cell) + (y / cell)) % 2 == 0;
                    int v = even ? 255 : 40;

                    int rgba = ((v & 0xff) << 24)
                            | ((v & 0xff) << 16)
                            | ((v & 0xff) << 8)
                            | 255;
                    pixmap.drawPixel(x, y, rgba);
                }
            }

            PixmapIOUtil.writePng(file, pixmap);
        } finally {
            pixmap.dispose();
        }
    }

    private static void generateHeight(FileHandle file, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        try {
            float cx = width / 2f;
            float cy = height / 2f;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float dx = (x - cx) / cx;
                    float dy = (y - cy) / cy;
                    float r = (float)Math.sqrt(dx * dx + dy * dy);

                    float wave = 0.5f + 0.5f * (float)Math.cos(r * 12f);
                    wave *= MathUtils.clamp(1f - r, 0f, 1f);

                    int v = MathUtils.clamp(Math.round(wave * 255f), 0, 255);
                    int rgba = ((v & 0xff) << 24)
                            | ((v & 0xff) << 16)
                            | ((v & 0xff) << 8)
                            | 255;
                    pixmap.drawPixel(x, y, rgba);
                }
            }

            PixmapIOUtil.writePng(file, pixmap);
        } finally {
            pixmap.dispose();
        }
    }
}