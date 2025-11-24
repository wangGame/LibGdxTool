package com.kw.gdx.singleton;

/**
 * 测试类
 */
public class App {
    public static void main(String[] args) {
        SingletonA instanceA1 = SingletonA.getInstance();
        System.out.println(instanceA1);
        SingletonB instanceB = SingletonB.getInstance();
        System.out.println(instanceB);
        SingletonA instanceA2 = SingletonA.getInstance();
        System.out.println(instanceA2);
    }
}
