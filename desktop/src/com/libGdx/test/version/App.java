package com.libGdx.test.version;

public class App {
    public static void main(String[] args) {
        System.out.println(App.class.getPackage().getImplementationVersion());
        System.out.println(App.class.getProtectionDomain().getCodeSource().getLocation());
    }
}
