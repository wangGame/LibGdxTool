package kw.artpuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.kw.gdx.BaseGame;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.resource.annotation.SpineResource;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.utils.log.StringUtils;
import com.kw.gdx.zip.PackZip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import kw.artpuzzle.asset.FontResource;
import kw.artpuzzle.asset.SpineRes;
import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.csv.InitCsvData;
import kw.artpuzzle.csv.LevelOrder;
import kw.artpuzzle.downLoad.DownLoadLevelTempUtils;
import kw.artpuzzle.flurry.FlurryUtils;
import kw.artpuzzle.listener.GameListener;
import kw.artpuzzle.net.DownLoad;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.artpuzzle.screen.LoadingScreen;

/**
 * 使用的画笔
 */
@GameInfo(width = 720,height = 1280,batch = Constant.COUPOLYGONBATCH)
public class ArtPuzzle extends BaseGame {
    private static Array<LevelOrder> taskLevelArray = new Array<>();
    private static Array<LevelOrder> currentTaskLevelArray = new Array<>();
    public ArtPuzzle(DownLoad downLoad,GameListener gameListener){
        GameStaticInstance.dispose();
        GameStaticInstance.downLoad = downLoad;
        GameStaticInstance.gameListener = gameListener;
        GameStaticInstance.gameInstance = this;
        NLog.isLog = false;
        for (int i = 0; i < 30; i++) {
            integerHashSet.add((i+1));
        }
        for (int i = 1; i <= 6; i++) {
            integerHashSet.add(30 + i * 5);
        }
        for (int i = 1; i <= 8; i++) {
            integerHashSet.add(60 + i * 15);
        }
        integerHashSet.add(240);
        integerHashSet.add(360);
        integerHashSet.add(420);
        integerHashSet.add(480);
        integerHashSet.add(540);
        integerHashSet.add(600);
    }

    @Override
    protected void loadingView() {
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        GameStaticInstance.dispose();
    }

    private HashSet<Integer> integerHashSet = new HashSet<>();
    private HashSet<Integer> alreadyIntegerHashSet = new HashSet<>();

    @Override
    public void render() {
        super.render();
        LevelConfig.gamePlayTime += Gdx.graphics.getDeltaTime();
        float v = LevelConfig.gamePlayTime / 60;
        int floor = (int)Math.floor(v);
        if (integerHashSet.contains(floor)) {
            if (!alreadyIntegerHashSet.contains(floor)){
                alreadyIntegerHashSet.add(floor);
                FlurryUtils.gameTimeTotal("gameTime-totalGameTime-"+floor);
            }
        }


        if (taskLevelArray.size<=0)return;
        if (currentTaskLevelArray.size<=0){
            currentTaskLevelArray.add(taskLevelArray.removeIndex(0));
            LevelOrder levelOrder = currentTaskLevelArray.get(0);
            DownLoadLevelTempUtils
                    downLoadLevelTempUtils
                    = new DownLoadLevelTempUtils(
                    this, levelOrder.getLevel_num(),
                    levelOrder.getVersion(),
                    new Runnable() {
                        @Override
                        public void run() {

                            if (currentTaskLevelArray.size>0) {
                                currentTaskLevelArray.removeIndex(0);
                            }
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {

                        }
                    }
            );
            downLoadLevelTempUtils.downLoad();
        }
    }

    //任务
    public void checkLevel(){
        int currentLevel = ArtPuzzlePreferece.getInstance().getCurrentLevel();
        int start = currentLevel;
        int num = 5;
        if (Configuration.device_state == Configuration.DeviceState.poor){
            num = 2;
        }
        int end = currentLevel+num;
        if (end>LevelConfig.MAXLEVEL){
            end = LevelConfig.MAXLEVEL;
        }
        ArrayMap<Integer, LevelOrder> orderHashMap = InitCsvData.orderHashMap;
        if (start ==1){
            start = 2;
        }
        for (int i = start; i < end; i++) {
            LevelOrder levelOrder = orderHashMap.get(i);
            String version = levelOrder.getVersion();
            String path = StringUtils.append(version,"/level/level",levelOrder.getLevel_num());
            FileHandle local = Gdx.files.local(path);
            boolean exists = local.exists();
            if (!exists){
                if (!taskLevelArray.contains(levelOrder,false)
                        &&!currentTaskLevelArray.contains(levelOrder,false)) {
                    taskLevelArray.add(levelOrder);
                }
                continue;
            }
            boolean check = false;
            try {
                check = PackZip.check(local);
            }catch (Exception e){
                if (i !=1) {
                    local.deleteDirectory();
                }
                check = false;
            }
            if (!check){
                taskLevelArray.add(levelOrder);
                local.deleteDirectory();
            }
        }

    }


}
