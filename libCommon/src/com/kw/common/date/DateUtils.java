package com.kw.common.date;

import java.io.DataInput;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    @Deprecated
    public void getCurrentInfoFromDate(){
        Date date = new Date();
        DateInfo info = new DateInfo();
        info.setYear(date.getYear());
        info.setMonth(date.getMonth());
        info.setDay(date.getDay());
        info.setHour(date.getHours());
        info.setMinutes(date.getMinutes());
        info.setSecond(date.getSeconds());
    }

    public void getCurrentInfoFromCalendar(){
        Calendar date = Calendar.getInstance();
        DateInfo info = new DateInfo();
        info.setYear(date.get(Calendar.YEAR));
        info.setMonth(date.get(Calendar.MONTH));
        info.setDay(date.get(Calendar.DAY_OF_MONTH));
        info.setHour(date.get(Calendar.HOUR));
        info.setMinutes(date.get(Calendar.MINUTE));
        info.setSecond(date.get(Calendar.SECOND));
    }

    /**
     * 相隔几天
     */
    private SimpleDateFormat formatter;
    public long diffDays(String startDate,String endDate){
        long diff = 0;
        // 定义日期解析格式
        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            Date start = formatter.parse(startDate);
            Date end = formatter.parse(endDate);
            long l = end.getTime() - start.getTime();
            diff = l / (1000 * 60 * 60 * 24);
            // 打印天数差
            System.out.println("日期差：" + diff + " 天");
        }catch (Exception e){

        }
        return diff;
    }

    public static String getETAString(final long etaInMilliSeconds) {
        if (etaInMilliSeconds < 0) {
            return "";
        }
        int seconds = (int) (etaInMilliSeconds / 1000);
        long hours = seconds / 3600;
        seconds -= (int) (hours * 3600);
        long minutes = seconds / 60;
        seconds -= (int) (minutes * 60);
        long day = hours / 24;
        if (day>0){
            return String.format("%02dd %02dh %02dm %02ds", day,hours%24, minutes, seconds);
        }else if (hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%02dm %02ds", minutes, seconds);
        } else {
            return String.format("%02ds", seconds);
        }
    }

    public static void main(String[] args) {
        System.out.println(getETAString(System.currentTimeMillis()));
    }
}
