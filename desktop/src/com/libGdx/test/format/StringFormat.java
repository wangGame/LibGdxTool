package com.libGdx.test.format;

import com.libGdx.test.base.LibGdxTestMain;

import org.lwjgl.Sys;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/12 18:25
 */
public class StringFormat extends LibGdxTestMain {
    public static void main(String[] args) {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        date.setHours(3);
        date.setMinutes(4);
        date.setSeconds(3);
//        String format1 = String.format("%tb:%2d :%2d", year, month, day);
//        System.out.println(format1);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateString = sdf.format(date);
        System.out.println(dateString);

        System.out.println(String.format("%tT", date));

    }
}
