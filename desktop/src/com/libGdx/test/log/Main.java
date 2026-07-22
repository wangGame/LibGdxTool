package com.libGdx.test.log;

public class Main {
    public static void main(String[] args) {
        ImageLoader loader = new ImageLoader();
        loader.loadImage();
        int result = ImageLoader.calculate(10, 20);
        System.out.println("result=" + result);
        try {
            loader.failure();

        } catch (IllegalStateException expected) {
            System.out.println("caught=" + expected.getMessage());
        }
    }
}
