package kw.artpuzzle.constant;

import com.badlogic.gdx.Gdx;

import kw.artpuzzle.shadermanager.ShaderType;

public class LevelConfig {
    public static final int RESET = 0;
    public static final int VIEW = 2;
    public static boolean LOCKANIMATION = false;
    /************* mainscreen level begin********/
    //游玩
    public static int ENTERGAME = 0;
    public static int PLAY = 0;
    public static int LOOK = 1;
    //预览
    public static final int LOCKGAME = 1;
    public static final int MAININITPOS = 0;

    public static final int MAININITPOS_MAX_LEVEL = 0;

    public static final int MAININITPOS_CURRENT_LEVEL = 0;


    public static int levelNum;
    public static int levelNumIndex;

    //未解锁
    public static int NOLOCK = 0;
    //已玩过
    public static int PLAYED = 1;
    //正在玩
    public static int PLAYING = 2;

    /************* mainscreen level end********/
    /************* level zip begin********/
    public static final String levelDirPath = "level/level";
    public static String mdFileName = "/md5apkold.txt";
    /************* level zip end********/
    /************* gameScreen begin********/
    public static boolean useInocal = true;
    public static int gameStatus = 0;
    /************* gameScreen end********/
    //is shuffle bottom pic!
    public static int ISSHUFFLE = 2;
    public static final int NOSHUFFLE = 0;
    public static final int SHUFFLE = 1;
    public static final int NISHUFFLE = 2;
    public static int enterAni = 5;
    public static ShaderType lineType;
    public static String devUrl = "http://192.168.1.192/artpuzzle/";

    public static String url = "https://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/";
    public static String desktopOrLowVersionUrl = "http://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/";


    public static boolean guide = false;
    public static boolean isFinshAll = false;
    public static int noInternetLevel = -1;
    public static float countTime = 0;


    public static float interstitial = 20;
    public static long leaveTime = 30 * 1000L;
//    version1/level/level1.zip

    public static boolean bannerStatus;
    public static int mainNeedLoadingAnmation = 0;

    public static int LoadingEnterMain = 0;
    public static int GameEnterMain = 1;

    public static int MAXLEVEL = 150;
    public static boolean enterGame;
    public static boolean isFristEnterGame;
    public static float tipDelay = 3;


    public static String forMatString(String format,String... args){
        return String.format(format,args);
    }

    public static final String levelPath1 = String.format("level/level%s",1);
    /*************  history base path********/
    public static final String historyBasePath = "levelHistory/";
    public static final String historyNumBathPath = "levelNumHistory/";

    public static float globalScale = 1;

    //game time
    public static float gameCurrentLevelPlayTime;
    public static float gamePlayTime;

    //进入游戏的次数
    public static int enterNum = 0;

    public static int dict[] = {
            120,180,240,300,360,420,480,540,600
    };

    public static float hintTime = 0;
    public static float hintTimeCount = 0;

}
