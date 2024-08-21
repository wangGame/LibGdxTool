package com.kw.gdx.thread;

import com.badlogic.gdx.Gdx;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 通过线程进行加载，完成之后进行同步
 */
public class ThreadUtils {
    private static Executor threadExecutor;
    private static ThreadUtils threadUtils;
    private static ExecutorService executorService;
    public static Executor getGlobalDeliver() {
        if (threadExecutor == null) {
            threadExecutor = new Executor() {
                @Override
                public void execute(Runnable command) {
                    Gdx.app.postRunnable(command);
                }
            };
        }
        return threadExecutor;
    }


    public ThreadUtils(){
        if (executorService == null){
            executorService = Executors.newFixedThreadPool(3);
        }
    }

    public static ThreadUtils getThreadUtils() {
        if (threadUtils == null){
            threadUtils = new ThreadUtils();
        }
        return threadUtils;
    }

    public <T> Task<T> doTask(Task<T> task){
        executorService.execute(task);
        return task;
    }
}
