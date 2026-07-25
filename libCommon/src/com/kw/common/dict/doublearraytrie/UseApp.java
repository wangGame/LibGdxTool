package com.kw.common.dict.doublearraytrie;

public class UseApp {
    public static void main(String[] args) {
        WordManager manager =
                new WordManager();


        manager.init();



        System.out.println(
                manager.check("aaapp")
        );


        System.out.println(
                manager.check("ant")
        );


        System.out.println(
                manager.check("hello")
        );
    }
}
