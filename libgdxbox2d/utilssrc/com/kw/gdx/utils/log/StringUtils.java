package com.kw.gdx.utils.log;

import java.util.Locale;

public class StringUtils {

    public static String format(String fmt, Object... args) {
        return String.format(Locale.getDefault(), fmt, args);
    }

    public static String formatRaceTime(float time) {
        int minutes = (int) (time / 60);
        int seconds = (int) (time) % 60;
        int millis = (int) (time * 1000) % 1000;
        return String.format(Locale.US, "%02d.%03d",  seconds, millis);
    }

    private static StringBuilder stringBuilder = new StringBuilder();
    public static String append(Object... arg){
        stringBuilder.setLength(0);
        for (Object s : arg) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public static String formatTime(Long time) {
        if (time==null){
            return null;
        }
        //时
        int hour = (int) (time / 60 / 60);
        //分
        int minutes = (int) (time / 60 % 60);
        //秒
        int remainingSeconds = (int) (time % 60);
        return String.format(Locale.US,"%02d : %02d : %02d",hour,minutes,remainingSeconds);
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.formatTime(10401L));
    }

}
