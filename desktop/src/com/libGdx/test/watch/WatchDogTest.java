package com.libGdx.test.watch;

import com.anrutils.example.ANRError;
import com.anrutils.example.ANRWatchDog;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * 仿照
 *
 * @Auther jian xian si qi
 * @Date 2023/12/26 10:19
 */
class WatchDogTest extends LibGdxTestMain {
    public static void main(String[] args)  {
        new WatchDogTest().start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ANRWatchDog anrWatchDog = new ANRWatchDog();
//        anrWatchDog.setIgnoreDebugger(true); //忽略
        anrWatchDog.setReportThreadNamePrefix("main"); //指定线程
        anrWatchDog.setReportMainThreadOnly(); //
        anrWatchDog.setANRListener(new ANRWatchDog.ANRListener() {
            @Override
            public void onAppNotResponding(ANRError error) {
                error.printStackTrace();
                System.out.println("------------------");
            }
        });
        anrWatchDog.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
