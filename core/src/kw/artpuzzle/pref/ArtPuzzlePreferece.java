package kw.artpuzzle.pref;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.kw.gdx.constant.Constant;

import kw.artpuzzle.constant.LevelConfig;
public class ArtPuzzlePreferece {
    private static ArtPuzzlePreferece preferece;
    private Preferences preferences;
    private ArtPuzzlePreferece(){
        preferences = Gdx.app.getPreferences("ArtPuzzle");
        if (preferences.getBoolean("isFristEnter",true)){
            LevelConfig.isFristEnterGame = true;
            preferences.putBoolean("isFristEnter",false);
            preferences.putLong("install_data",System.currentTimeMillis());
            preferences.flush();
        }else {
            LevelConfig.isFristEnterGame = preferences.getBoolean("isFristEnter");
        }
    }

    public long getInstallData(){
        if (!preferences.contains("install_data")){
            preferences.putLong("install_data",System.currentTimeMillis());
            preferences.flush();
        }


        return preferences.getLong("install_data",System.currentTimeMillis());
    }

    public static ArtPuzzlePreferece getInstance(){
        if (preferece==null){
            preferece = new ArtPuzzlePreferece();
        }
        return preferece;
    }

    public void updateScore(int score){
        preferences.putInteger("score",getScoreNum()+score);
        preferences.flush();
    }

    public int getScoreNum(){
        return preferences.getInteger("score",0);
    }

    public void saveCurrentLevel(int level){
        if (level<getCurrentLevel())return;
        preferences.putInteger("level_play",level);
        preferences.flush();
    }

    public int getCurrentLevel(){
        int level_play = preferences.getInteger("level_play", 1);
        if (level_play>150){
            return 150;
        }
        return preferences.getInteger("level_play",1);
    }

    public void upFinish(){
        preferences.putBoolean("isFinish",true);
        preferences.flush();
        LevelConfig.isFinshAll = preferences.getBoolean("isFinish",false);
    }

    public void isFinsh(){
        LevelConfig.isFinshAll = preferences.getBoolean("isFinish",false);
    }

    public void addLevelNum() {
        LevelConfig.levelNum++;
        LevelConfig.levelNum = LevelConfig.levelNum > 150 ? 151 : LevelConfig.levelNum;
        if(LevelConfig.levelNum>150){
            upFinish();
        }
        saveCurrentLevel(LevelConfig.levelNum);
    }

    public boolean isMusic() {
        return preferences.getBoolean("music");
    }

    public void updateMusic(){
        boolean sound = isMusic();
        preferences.putBoolean("music",!sound);
        preferences.flush();
    }

    public boolean isSound(){
        Constant.isSound = preferences.getBoolean("setting_soundSwitch",true);
        return Constant.isSound;
    }

    public void updateSwitch(String key) {
        boolean isEffect = isEffect(key);
        preferences.putBoolean(key,!isEffect);
        preferences.flush();
    }

    public boolean isEffect(String key) {
        return preferences.getBoolean(key,true);
    }

    public boolean isHaptic() {
        Constant.isHaptic = preferences.getBoolean("setting_hapticSwitch",true);
        return Constant.isHaptic;
    }

    public boolean isGuide() {
        return preferences.getBoolean("isguide",true);
    }

    public void updateGuide() {
        preferences.putBoolean("isguide",false);
        preferences.flush();
        LevelConfig.guide = false;
    }

    public boolean isRate() {
        return preferences.getBoolean("isRate",false);
    }

    public void saveRate() {
        preferences.putBoolean("isRate",true);
    }

    public boolean isHideLevelRate() {
        return preferences.getBoolean("hideLevelRate",false);
    }

    public void hideLevelRate() {
        preferences.putBoolean("hideLevelRate",true);
        preferences.flush();
    }

    public void updateHintStatus(boolean status) {
        preferences.putBoolean("hintStatus",status);
        preferences.flush();
    }

    public boolean isHintStatus(){
        return preferences.getBoolean("hintStatus",false);
    }

    public boolean isGameLevelGuide() {
        return preferences.getBoolean("levelGuide",false);
    }

    public void updateGameLevelGuide() {
        preferences.putBoolean("levelGuide",true);
        preferences.flush();
    }

}

