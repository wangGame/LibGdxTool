package com.libGdx.tools.unplist.atals;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 *
 */
public class UnTexturepacker extends Game {
    public static void main(String[] args) throws Exception {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 600;
        config.height = 400;
        config.title = "Temp Viewer";
        new LwjglApplication(new UnTexturepacker(), config);
    }

    @Override
    public void create() {
        String arr[] = {"tb"};
        for (int i = 0; i < arr.length; i++) {
            String path = "atlas/" + arr[i];

            File file = new File("atlas/"+arr[i]+"/");
            file.mkdir();

            FileHandle packFile = Gdx.files.internal(path+".atlas");
            TextureAtlas.TextureAtlasData textureAtlasData = new TextureAtlas.TextureAtlasData(packFile, packFile.parent(), false);
            Untext untext = new Untext();
            untext.splitAtlas(textureAtlasData,"out/"+arr[i]);
        }
        System.out.println("全部结束");
    }
}
