package com.kw.gdx.constant;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.BaseScreen;

public class Constant {
    public static boolean SHOWRENDERCALL = false; //render call
    public static boolean SHOWFRAMESPERSECOND = false; //展示fps
    public static boolean DEBUG = false;
    public static boolean realseDebug = true;
//    viewport
    public static final int EXTENDVIEWPORT = 0;
    public static final int FITVIEWPORT = 1;
//   batch
    public static final int COUPOLYGONBATCH = 0;
    public static final int SPRITEBATCH = 1;
//  assetManager Type
    public static int ASSETMANAGERTYPE = 0;
    //
    public static double gameDensity = 2;
    //设计尺寸
    public static float WIDTH = 1080;
    public static float HIGHT = 1920;
    //标准尺寸
    public static final float STDWIDTH = 1080;
    public static final float STDHIGHT = 1920;
    //实际使用
    public static float GAMEWIDTH = 1080;
    public static float GAMEHIGHT = 1920;

    public static Color viewColor = new Color(0,0,0,1);
    public static int batchType = 0;
    public static int viewportType = 0;
    public static boolean isSound = true;
    public static float soundV = 1;
    public static boolean isMusic = true;
    public static BaseScreen currentActiveScreen;

    public static void updateInfo(GameInfo info){
        if (info == null)return;
        Constant.WIDTH = info.width();
        Constant.HIGHT = info.height();
        Constant.batchType = info.batch();
        Constant.viewportType = info.viewportType();
    }

    public static void updateSize(Viewport stageViewport) {
        if (stageViewport == null)return;
        Constant.GAMEWIDTH = stageViewport.getWorldWidth();
        Constant.GAMEHIGHT = stageViewport.getWorldHeight();
    }

    public static float maxScale(){
        return Math.max(Constant.GAMEWIDTH/Constant.WIDTH,Constant.GAMEHIGHT/Constant.HIGHT);
    }
}
