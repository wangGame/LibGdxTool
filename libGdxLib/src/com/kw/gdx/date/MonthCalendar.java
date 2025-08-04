package com.kw.gdx.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MonthCalendar {
    public boolean isNetTime;
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    private String timeziD = "GMT-1";
    private static MonthCalendar bean;

    public static MonthCalendar dateBean() {
        if (bean == null) {
            bean = new MonthCalendar();
        }
        Calendar instance  = Calendar.getInstance();
        bean.isNetTime  = true;
        bean.year       = instance.get(Calendar.YEAR);
        bean.month      = instance.get(Calendar.MONTH);
        bean.day        = instance.get(Calendar.DAY_OF_MONTH);
        bean.hour       = instance.get(Calendar.HOUR_OF_DAY);
        bean.minute     = instance.get(Calendar.MINUTE);
        bean.second     = instance.get(Calendar.SECOND);
        return bean;
    }

    public void timeShowUpdate(){
        GameTime gameTime = GameTime.getGameTime();
        gameTime.showFloat(currentDayEndTime());
    }

    public void timeDistance(){
        // 获取当前时间
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone(timeziD));
        // 构造今天 23:59:59 的时间
        Calendar endOfDay = Calendar.getInstance(TimeZone.getTimeZone(timeziD));
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 0);

        // 计算时间差（毫秒）
        long diffMillis = endOfDay.getTimeInMillis() - now.getTimeInMillis();
        if (diffMillis < 0) {
            System.out.println("已经过了今天的 23:59:59");
            return;
        }

        long totalSeconds = diffMillis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(timeziD));

        System.out.println("当前时间: " + sdf.format(now.getTime()));
        System.out.println("今天结束: " + sdf.format(endOfDay.getTime()));
        System.out.printf("时间差为: %d 小时 %d 分钟 %d 秒\n", hours, minutes, seconds);

    }

    public long currentDayEndTime(){
        Calendar endOfDay = Calendar.getInstance(TimeZone.getTimeZone(timeziD));
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 0);
        return endOfDay.getTimeInMillis();
    }

    public long currentTime(){
        return Calendar.getInstance(TimeZone.getTimeZone(timeziD)).getTimeInMillis();
    }

    @Override
    public String toString() {
        return "" + year +
                ":" + month +
                ":" + day;
    }

    public void setTimeziD(String timeziD) {
        this.timeziD = timeziD;
    }
}
