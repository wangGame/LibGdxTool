package com.kw.gdx.crash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class CrashUtils {

    public CrashUtils() {
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(
                            Thread thread, Throwable throwable) {

                        StackTraceElement element =
                                parseThrowable("", throwable);

                        if (element != null) {
                            System.out.println(element.getLineNumber());
                            System.out.println(element.getClassName());
                            System.out.println(element.getFileName());
                            System.out.println(element.getMethodName());
                            System.out.println(throwable.getClass().getName());
                        }

                        writeToFile(thread, throwable, "LibUtilscrash/");
                    }
                }
        );
    }

    /**
     * 把 Throwable 解析成 StackTraceElement
     */
    public static StackTraceElement parseThrowable(
            String packageName, Throwable ex) {

        if (ex == null
                || ex.getStackTrace() == null
                || ex.getStackTrace().length == 0) {
            return null;
        }

        for (StackTraceElement element : ex.getStackTrace()) {
            if (element.getClassName().contains(packageName)) {
                return element;
            }
        }

        return ex.getStackTrace()[0];
    }

    public void writeToFile(
            Thread thread, Throwable ex, String folder) {

        Calendar timeServer = Calendar.getInstance();

        int year = timeServer.get(Calendar.YEAR);
        int month = timeServer.get(Calendar.MONTH) + 1;
        int day = timeServer.get(Calendar.DAY_OF_MONTH);
        int hour = timeServer.get(Calendar.HOUR_OF_DAY);
        int minute = timeServer.get(Calendar.MINUTE);
        int second = timeServer.get(Calendar.SECOND);
        int millisecond = timeServer.get(Calendar.MILLISECOND);

        String fileName = "Crash " + year + "-"
                + month + "-" + day + " "
                + hour + "-" + minute + "-"
                + second + "-" + millisecond + ".txt";
        System.err.println("write crash log: " + folder + fileName);
        // LibGDX 本地私有存储目录
        FileHandle file = Gdx.files.local(folder + fileName);

        try (PrintWriter writer = new PrintWriter(
                new java.io.OutputStreamWriter(
                        file.write(false),
                        java.nio.charset.StandardCharsets.UTF_8))) {

            writer.println("Exception in thread \"" + thread.getName() + "\"");
            ex.printStackTrace(writer);
            writer.flush();

        } catch (Exception e) {
            System.err.println("Failed to write crash log: " + e);
        }
    }
}