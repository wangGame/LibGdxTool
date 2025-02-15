package com.kw.gdx.constant;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.BaseScreen;

public class Constant {
    public static boolean crashlog;
    public static String SDPATH = "/";
    public static boolean SHOWRENDERCALL = false; //render call
    public static boolean SHOWFRAMESPERSECOND = false; //展示fps
    public static boolean DEBUG = false;
    public static boolean realseDebug = true;
//    viewport
    public static final int EXTENDVIEWPORT = 0;
    public static final int FITVIEWPORT = 1;
    public static final int SCREENVIEWPORT = 2;
    public static final int FILLVIEWPORT = 3;
    public static final int STRETCHVIEWPORT = 4;
    public static final int SCALINGVIEWPORTX = 5;
    public static final int SCALINGVIEWPORTY = 6;


    //   batch
    public static final int COUPOLYGONBATCH = 0;
    public static final int SPRITEBATCH = 1;
//  assetManager Type
    public static int ASSETMANAGERTYPE = 0;
    //
    public static double gameDensity = 2;
    //设计尺寸
    public static float WIDTH = 11;
    public static float HIGHT = 11;
    //标准尺寸
    public static final float STDWIDTH = 11;
    public static final float STDHIGHT = 11;
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
    public static Camera camera;

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
