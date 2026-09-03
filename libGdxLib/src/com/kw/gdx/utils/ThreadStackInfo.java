package com.kw.gdx.utils;

public class ThreadStackInfo {
    public static void printStackInfo(int depth) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (int i = 2; i < Math.min(stack.length, depth); i++) {
            System.out.println("\tat " + stack[i]);
        }
    }
}
