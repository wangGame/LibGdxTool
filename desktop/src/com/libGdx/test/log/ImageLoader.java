package com.libGdx.test.log;


import com.example.methodtime.MethodTime;

public class ImageLoader {
    @MethodTime("loadImage")
    public void loadImage() {
        sleep(25);
        privateWork();
    }

    @MethodTime(value = "privateWork", thresholdMs = 1)
    private void privateWork() {
        sleep(8);
    }

    @MethodTime("calculate")
    public static int calculate(int a, int b) {
        sleep(5);
        return a + b;
    }

    @MethodTime("failure")
    public void failure() {
        sleep(3);
        throw new IllegalStateException("test failure");
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
