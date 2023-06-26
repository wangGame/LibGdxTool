package kw.artpuzzle.constant;

import com.badlogic.gdx.utils.Disposable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.listener.GameListener;
import kw.artpuzzle.net.DownLoad;

public class GameStaticInstance {
    public static ExecutorService fixedThreadPool;
    public static ArtPuzzle gameInstance;
    public static DownLoad downLoad;
    public static GameListener gameListener;


    public static void dispose() {
        if (fixedThreadPool != null) {
            fixedThreadPool.shutdown();
        }
        fixedThreadPool = null;
        gameInstance = null;
        downLoad = null;
        gameListener = null;
    }

    public static ExecutorService getFixedThreadPool() {
        if (fixedThreadPool == null){
           fixedThreadPool = Executors.newFixedThreadPool(1);
        }
        return fixedThreadPool;
    }
}
