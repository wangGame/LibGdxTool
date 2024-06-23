package com.kw.gdx.abtest;

public class ABTest {
    public static String currentV = "B";
    public static boolean isVersion(String name){
        if (name==null)return false;
        if (currentV.equalsIgnoreCase(name)) {
            return true;
        }else {
            return false;
        }
    }
}
