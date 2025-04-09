package com.kw.gdx.abtest;

public class ABTest {
    public static String currentV = "A";

    public static boolean isVersion(String name){
        if (name==null)return false;
        if ((name.startsWith("A"))&& currentV.equals("")){
            return true;
        }
        if (currentV.startsWith(name)) {
            return true;
        }else {
            return false;
        }
    }

    public static boolean subVersion(String name){
        if (name==null)return false;
        if (currentV.equals("")){
            if (name.endsWith("A")){
                return true;
            }
            return false;
        }
        if (currentV.endsWith(name)) {
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) {
        ABTest abTest = new ABTest();
        abTest.currentV = "BA";

        System.out.println(isVersion("A"));
        System.out.println(isVersion("B"));

        System.out.println(subVersion("A"));
        System.out.println(subVersion("B"));


    }
}
