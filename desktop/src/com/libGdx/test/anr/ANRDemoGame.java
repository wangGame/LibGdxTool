package com.libGdx.test.anr;

import com.badlogic.gdx.Game;
import com.kw.gdx.anr.ANRWatchDog;


public class ANRDemoGame extends Game {


    private ANRWatchDog watchDog;


    @Override
    public void create() {


        /*
         * 注意：
         *
         * 这里是LibGDX主线程
         *
         * ANRWatchDog内部:
//         *
//         * targetThread = Thread.currentThread()
         *
         * 会记录正确线程
         *
         */


        watchDog =
                new ANRWatchDog(3000);



        watchDog

                //ANR发生监听
                .setANRListener(error -> {


                    System.out.println(
                            "======================"
                    );


                    System.out.println(
                            "检测到ANR"
                    );


                    System.out.println(
                            error.getMessage()
                    );


                    System.out.println(
                            "卡顿时间:"
                                    +
                                    error.duration
                                    +
                                    "ms"
                    );



                    error.printStackTrace();



                })



                //报告所有线程
                .setReportAllThreads()



                //线程没有堆栈也打印
                .setLogThreadsWithoutStackTrace(true)



                //是否忽略debug
                .setIgnoreDebugger(false);



        watchDog.start();



        setScreen(
                new ANRScreen()
        );


    }



    @Override
    public void dispose() {


        if(watchDog!=null){

            watchDog.interrupt();

        }


        super.dispose();

    }

}
