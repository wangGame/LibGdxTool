package com.anrutils;

import com.anrutils.example.ANRError;
import com.anrutils.example.ANRWatchDog;

/**
 * 对https://github.com/SalomonBrys/ANR-WatchDog/进行变化，
 *
 *
 */
public class App {
    public static void main(String[] args) {

        //使用案例    检测器的时间设置为3001 毫秒，如果超过了这个时间就会抛出异常。
        ANRWatchDog anrWatchDog = new ANRWatchDog(3001);
        anrWatchDog.start();
        anrWatchDog.setReportThreadNamePrefix("APP---------------------------- ");
//        anrWatchDog.setANRListener(new ANRWatchDog.ANRListener() {
//            @Override
//            public void onAppNotResponding(ANRError error) {
////                ExceptionHandler.saveException(error,new CrashManaer);
//            }
//        });
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
