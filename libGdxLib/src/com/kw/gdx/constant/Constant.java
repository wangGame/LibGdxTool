package com.kw.gdx.constant;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.BaseScreen;

public class Constant {
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
    public static final int INTERNAL = 0;
    public static final int LOCAL = 1;
    public static final int BOTHASSET = 2;
    //
    public static double vvv = 2;
//
    public static float WIDTH = 1080;
    public static float HIGHT = 1920;
    public static float GAMEWIDTH = 1080;
    public static float GAMEHIGHT = 1920;
    public static final float STDWIDTH = 1080;
    public static final float STDHIGHT = 1920;

    public static Color viewColor = new Color(1,1,1,1);
    /**
     * 0 : cpubatch
     *
     */
    public static int batchType = 0;
    public static int viewportType = 0;
    public static boolean isSound = true;
    public static boolean isHaptic = true;
    public static float soundV = 1;
    public static boolean isMusic = true;
    public static boolean showInterstitial;

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

    public static BaseScreen currentScreen;

    public static int jishu = 0;
}
