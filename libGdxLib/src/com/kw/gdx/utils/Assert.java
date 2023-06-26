package com.kw.gdx.utils;

import com.kw.gdx.utils.log.NLog;

public class Assert {
    public static void checkError(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void checkInfo(boolean condition,String message){
        if (!condition) {
            NLog.check(message);
        }
    }
}
