package kw.artpuzzle.downLoad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectSet;
import com.kw.gdx.BaseGame;
import com.kw.gdx.utils.log.NLog;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.constant.GameStaticInstance;

public class BaseDownLoadUtils {
    protected String resourceVersion;
    protected String pre;
    protected StringBuilder stringBuilder ;
    protected ArtPuzzle game;
    protected int levelPath;
    protected Runnable runnable;
    protected Runnable retryRunnable;
    protected HashMap<String,Integer> tryNumCount = new HashMap<>();

    protected String siteusing;
    protected String fromPath;
    protected String toPath;
    protected DownLoadListener success;
    protected DownLoadListener failed;
//    protected ExecutorService fixedThreadPool;
//    fixedThreadPool = Executors.newFixedThreadPool(1);

    public BaseDownLoadUtils(BaseGame game, Runnable runnable,Runnable retryRunnable){
        this.game = (ArtPuzzle) game;
        this.runnable = runnable;
        this.retryRunnable = retryRunnable;
        stringBuilder = new StringBuilder();
    }


    protected String append(Object... arg){
        stringBuilder.setLength(0);
        for (Object s : arg) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }


    protected void downFile() {
        NLog.e("start down load ...");
        BaseGame.setText("start down load ...");
        GameStaticInstance.downLoad.downloadOneFile(
                siteusing,
                fromPath,
                toPath,
                success,
                failed);
    }

    protected void reDownLoad(boolean copyFlag) {
        if (!copyFlag){
            downLoad();
        }
    }

    public void downLoad() {
        if (retry()) {
            NLog.e("try out times");
            if (retryRunnable!=null) {
                retryRunnable.run();
            }
            return;
        }
        downFile();
    }

    protected boolean retry(){
        return false;
    }

    protected void clearDir() {

    }

    public abstract static class DownLoadListener{
        public void downLoadCallBack(){
        }

        public void downLoadCallBack(int code){

        }
    }
}
