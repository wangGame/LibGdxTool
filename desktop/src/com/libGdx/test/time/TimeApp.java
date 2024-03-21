package com.libGdx.test.time;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.utils.Timer;
import com.kw.gdx.utils.TimerTask;
import com.libGdx.test.base.LibGdxTestMain;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeApp extends LibGdxTestMain {
    public static void main(String[] args) {
        TimeApp app = new TimeApp();
        app.start();
    }

    private long startTime;
    private long l;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 创建计时器
        l = converServerTime();
        startTime = startTime();
        showEnd(startTime - l);

//        执行定时任务（比如延迟1秒后执行任务）
//        timer.scheduleTask(new Timer.Task() {
//            @Override
//            public void run() {
//                // 在这里编写你的任务代码
//                System.out.println("任务执行了！");
//            }
//        }, 1); // 延迟1秒后执行任务


//        String str = "Fri, 23 Feb 2024 08:14:55 GMT";
        Timer timer = new Timer();
// 可选：设置计时器为无限循环执行
//        timer.schedule();

// 可选：设置计时器的重复次数和间隔时间（比如每隔0.5秒执行一次，总共执行5次）
        timer.schedule(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                // 在这里编写你的任务代码
                l += 1000;
                showEnd(startTime - l);
            }
        }, 0, 1000); // 延迟1秒后执行第一次任务，之后每隔0.5秒执行一次

        // 在需要停止计时器时，可以调用stop()方法
        // timer.stop();
    }

    private void showEnd(long millis) {
        if (millis<=0){
            //结束  非法用户直接结束
        }
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        System.out.println(
                String.format("%d %02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60));
    }

    public static long converServerTime(){
        // 创建用于解析日期字符串的格式化程序
        try {
            String str = "Fri, 23 Feb 2024 08:14:55 GMT";
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            Date date = formatter.parse(str);
            System.out.println(
                    (date.getYear()+1900) +
                            "-"+
                            (date.getMonth()+1) +
                            "-"+date.getDate() +"  "+
                            date.getHours() + " : " +
                            date.getMinutes() + " : " +
                            date.getSeconds());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }



    public long startTime(){
        Date date = new Date();
        date.setYear(2024-1900);
        date.setMonth(1);
        date.setDate(23 + 20);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date.getTime();
    }
}
