package com.kw.gdx.thread;

import java.util.concurrent.Executor;

public class Task<T> implements Runnable {

    @Override
    public void run() {
        T t = doRunnable();
        if (t != null){
            Executor globalDeliver = ThreadUtils.getGlobalDeliver();
            globalDeliver.execute(new Runnable() {
                @Override
                public void run() {
                    success(t);
                }
            });
        }
    }

    public T doRunnable(){
        return null;
    }

    public void success(T t){

    }
}
