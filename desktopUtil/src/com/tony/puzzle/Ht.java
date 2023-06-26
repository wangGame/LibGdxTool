package com.tony.puzzle;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Ht {
    public static void main(String[] args) {
        texturePack();
//        jiami();
//        jiemi();
    }
    static String[] atlasFileName = {
//            "level2","level2-1",
//            "level3","level3-1"
//        "level4","level4-1",
//            "level5","level5-1",
//            "level6","level6-1",
//              "level8","level8-1",
//            "level7","level7-1",
//            "level8","level8-1",
//            "level9","level9-1",
//            "level10","level10-1",
//            "level11","level11-1",
//            "level12","level12-1",
//            "level13","level13-1",
//            "level14","level14-1",
//            "level15","level15-1",
//            "level16","level16-1",
//            "level17","level17-1",
//            "level18","level18-1",
//            "level19","level19-1",
//            "level20","level20-1",
//            "level21","level21-1",
//            "level22","level22-1",
//            "level23","level23-1",
//            "level24","level24-1",
//            "level25","level25-1",
//            "level26","level26-1",
//            "level27","level27-1",
//            "level28","level28-1",
//            "level29","level29-1",
//            "level30","level30-1",
//            "level31","level31-1",
//            "level32","level32-1",
//            "level33","level33-1",
//            "level34","level34-1",
//            "level35","level35-1",
//            "level36","level36-1",
//            "level37","level37-1",
//            "level38","level38-1",
//            "level39","level39-1",
//            "level40","level40-1",
//            "level41","level41-1",
//            "level42","level42-1",
//            "level43","level43-1",
//            "level44","level44-1",
//            "level45","level45-1",
//            "level46","level46-1",
            "level64"

    };
    private static void texturePack() {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.pot = false;
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        settings.duplicatePadding = true;
        settings.paddingX = 12;
        settings.paddingY = 12;
        settings.bleed = true;
        settings.combineSubdirectories = true;
        settings.format = Pixmap.Format.RGBA8888;
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.useIndexes = false;

        settings.edgePadding = true;
        processAndroid(settings);
    }

    private static void processAndroid(TexturePacker.Settings setting) {
        for (int i = 0; i < atlasFileName.length; i++) {
            String input = atlasFileName[i];
            if (input == null) return;
            try {
                String outname = "levelAtlas";
                if (input.endsWith("-1")){
                    outname = "line";
                }
                TexturePacker.process(
                        setting,
                        "downlevel/ht/src/"+input + "/",
                        "downlevel/ht/out/"+input+"/" ,
                        /*"line"*/outname);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static void jiami(){
        String PicImage[] = {
                "level1",
                "level2",
                "level3",
        };
        for (int i = 0; i < PicImage.length; i++) {
            String input = PicImage[i];
            char[] miyao = {'t','o','n','y'};
            if (input == null) return;
            try {
                new File("downlevel/ht/jiami/"+input).mkdirs();
                FileInputStream fileInputStream = new FileInputStream("downlevel/ht/out/"+input+"/levelAtlas.png");
                FileOutputStream fileOutputStream = new FileOutputStream("downlevel/ht/jiami/"+input+"/levelAtlas.png");
                int read = 0;
                int begin = 10;
                int jishu = 0;
                int miyaoIndex = 0;
                while ((read = (fileInputStream.read()))!=-1){
                    if (jishu>begin && miyaoIndex< miyao.length){
                        fileOutputStream.write(read ^ miyao[miyaoIndex]);
                        miyaoIndex++;
                    }else {
                        fileOutputStream.write(read);
                        jishu++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void jiemi(){
        String PicImage[] = {
                "level1",
                "level2",
                "level3",
        };
        for (int i = 0; i < PicImage.length; i++) {
            String input = PicImage[i];
            char[] miyao = {'t','o','n','y'};
            if (input == null) return;
            try {
                new File("downlevel/ht/jiemi/"+input).mkdirs();
                FileInputStream fileInputStream = new FileInputStream("downlevel/ht/jiami/"+input+"/levelAtlas.png");
                FileOutputStream fileOutputStream = new FileOutputStream("downlevel/ht/jiemi/"+input+"/levelAtlas.png");
                int read = 0;
                int begin = 10;
                int jishu = 0;
                int miyaoIndex = 0;
                while ((read = (fileInputStream.read()))!=-1){
                    if (jishu>begin && miyaoIndex< miyao.length){
//                        fileOutputStream.write(read ^ miyao[miyaoIndex]);
                        miyaoIndex ++;
                    }else {
                        fileOutputStream.write(read);
                        jishu++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
