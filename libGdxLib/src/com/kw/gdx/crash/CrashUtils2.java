package com.kw.gdx.crash;

import com.badlogic.gdx.Gdx;

public class CrashUtils2 {
    public static void installCrashLogger() {
        final Thread.UncaughtExceptionHandler prior = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                try {
                    java.io.StringWriter sw = new java.io.StringWriter();
                    e.printStackTrace(new java.io.PrintWriter(sw));
                    Gdx.files.local("crash-log.txt").writeString("crash thread=" + t.getName() + "\n" + sw + "\n", true);
                } catch (Throwable ignored) {
                }
                if (prior != null) {
                    prior.uncaughtException(t, e);
                }
            }
        });
    }
}
