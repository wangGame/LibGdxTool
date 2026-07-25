package com.kw.common.dict.doublearraytrie;

public class UseApp {
    public static void main(String[] args) {
        WordManager manager =
                new WordManager();


        manager.init();



        System.out.println(
                manager.check("apple")
        );


        System.out.println(
                manager.check("banana")
        );


        System.out.println(
                manager.check("hello")
        );
    }
}
