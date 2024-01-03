package com.kw.gdx.utils;

import com.kw.gdx.utils.log.StringUtils;

/**
 * @Auther jian xian si qi
 * @Date 2024/1/2 15:49
 */
public class TimeUtils {

    public String timeStringHMS(int diff){
        int sec = diff % 60;
        int min = diff / 60 % 60;
        int hour = diff / 3600;
        if (hour > 24) {
            hour %= 24;
        }
        return StringUtils.format("%s%02d:%02d:%02d", hour, min, sec);
    }

    public String timeStringDHMS(int diff){
        int sec = diff % 60;
        int min = diff / 60 % 60;
        int hour = diff / 3600;
        String day = "";
        if (hour > 24) {
            day = (hour / 24) + "D:";
            hour %= 24;
        }
        return StringUtils.format("%s%02d:%02d:%02d", day, hour, min, sec);
    }

    public String timeStringDHM_S(int diff){
        int sec = diff % 60;
        int min = diff / 60 % 60;
        int hour = diff / 3600;
        String day = "";
        if (hour > 24) {
            day = (hour / 24) + "D:";
            hour %= 24;
            return StringUtils.format("%s%02d:%02d", day, hour, min);
        }else {
            return StringUtils.format("%s%02d:%02d", hour, min, sec);
        }
    }


}
