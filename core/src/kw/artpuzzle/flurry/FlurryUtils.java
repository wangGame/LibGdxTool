package kw.artpuzzle.flurry;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.kw.gdx.utils.log.NLog;

import java.util.HashMap;
import java.util.Map;

import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.pref.ArtPuzzlePreferece;

public class FlurryUtils {
    private static FlurryListener listener;

    public static void setLiterer(FlurryListener literer) {
        FlurryUtils.listener = literer;
    }

    public static void fristStartLoading() {
        f("guide_loading",  "first_start_loading", "guide");
    }

    public static void fristEndLoading() {
        f("guide_loading",  "first_end_loading", "guide");
    }

    public static void guideStepNum(int num) {
        if (LevelConfig.guide && !ArtPuzzlePreferece.getInstance().isGameLevelGuide()) {
            ArtPuzzlePreferece.getInstance().isGameLevelGuide();
            f("guide_step", num + "", "guide");
        }
    }

    public static void levelId(String num) {
        f("levelid",  num, "levelStart");
    }

    public static void gameId(int num) {
        f("gameid",  num+"", "levelStart");
    }
    ////////////
    public static void total(String num) {
        f("total",  num, "levelStart");
    }

    public static void hint(int num) {
        f("levelid",  num+"", "hint");
    }

    public static void roundTime(String num) {
        f("round_time",  num, "levelExit");
    }

    public static void levelExitLevelid(String value){
        f("levelid",  value, "levelExit");
    }

    public static void levelExitGameid(int value){
        f("gameid",  value+"", "levelExit");
    }

    public static void levelExitTotal(String value){
        f("total",  value+"", "levelExit");
    }

    public static void gameTimeTotal(String value){
        f("totalGameTime",  value, "gameTime");
    }

    public static void uiSoundOn(){
        f("sound",  "soundOn", "uiInteraction");
    }

    public static void uiSoundOff(){
        f("sound",  "soundOff", "uiInteraction");
    }

    public static void uiHapicOn(){
        f("hapic",  "hapicOn", "uiInteraction");
    }

    public static void uiHapicOff(){
        f("hapic",  "hapicOff", "uiInteraction");
    }

    public static void uiView(String levelid){
        f("view",  levelid, "uiInteraction");
    }

    public static void uiRestart(String levelid){
        f("restart",  levelid, "uiInteraction");
    }

    public static void adShowInsert(){
        f("insert",  "adpoints", "adShow");
    }

    public static void adShowVideo(){
        f("video",  "adpoints", "adShow");
    }

    public static void adClosedVideo(){
        f("video",  "adpoints", "adClosed");
    }

    public static void adClosedInsert(){
        f("insert",  "adpoints", "adClosed");
    }

    public static void adsInterstitial(long num){
        f("interstitial",  num+"", "ads");
    }

    public static void adsRewarded(long num){
        f("rewarded",  num+"", "ads");
    }

    public static void gameExitGameRound(String num){
        f("gameRound",  num, "gameExit");
    }

    public static void gameEnterEnter(){
        f("enter",  "enterGame", "enter");
    }





    public static interface FlurryListener {
        public void logEvent(String e, Map<String, String> map);
    }

    private static void f(String k, String v, String e) {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            Map<String, String> m = new HashMap<String, String>(1);
            m.put(k, v);
            if (listener != null) {
                listener.logEvent(e, m);
                NLog.e("Flurry Log Android","k:"+k+" v:"+v+" e:"+e);
            }
        }else if (Gdx.app.getType() == Application.ApplicationType.Desktop){
            System.out.printf("flurry log desktop - k:%s v:%s  e: %s  \n",
                    k,v,e);
//            System.err.printf("%s - %s - %s - %s\n",
//                    StringUtils.formatRaceTime((float)timeSpent), levelString, tag, message);
        }
    }
}