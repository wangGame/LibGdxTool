package com.kw.gdx.crash;

import com.kw.gdx.constant.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class CrashUtils {
    public CrashUtils(){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Throwable cause = throwable.getCause();
                StackTraceElement element = parseThrowable("", throwable);
                if (element == null) return;
                System.out.println(element.getLineNumber());
                System.out.println(element.getClassName());
                System.out.println(element.getFileName());
                System.out.println(element.getMethodName());
                System.out.println(throwable.getClass().getName());
                writeToFile(thread,throwable,"crash/");
            }
        });
    }

    /**
     * 把Throwable解析成StackTraceElement
     */
    public static StackTraceElement parseThrowable(String packageName, Throwable ex) {
        if (ex == null || ex.getStackTrace() == null || ex.getStackTrace().length == 0) return null;
        StackTraceElement element;
        for (StackTraceElement ele : ex.getStackTrace()) {
            if (ele.getClassName().contains(packageName)) {
                element = ele;
                return element;
            }
        }
        element = ex.getStackTrace()[0];
        return element;
    }

    public void writeToFile(Thread thread, Throwable ex, String folder) {
        Calendar timeServer = Calendar.getInstance();
        int yea = timeServer.get(Calendar.YEAR);
        int mon = timeServer.get(Calendar.MONTH) + 1;
        int day = timeServer.get(Calendar.DAY_OF_MONTH);
        int hou = timeServer.get(Calendar.HOUR_OF_DAY);
        int min = timeServer.get(Calendar.MINUTE);
        int sec = timeServer.get(Calendar.SECOND);
        int mil = timeServer.get(Calendar.MILLISECOND);
        String fname = "Crash " + yea + "-" + mon + "-" + day + " " + hou + "-" + min + "-" + sec + "-" + mil;
        File fh = new File(Constant.SDPATH, folder + "/" + fname + ".txt");
        try {
            if (!fh.getParentFile().exists())
                fh.getParentFile().mkdirs();
            fh.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter br = null;
        try {
            br = new PrintWriter(new FileWriter(fh));
            br.write("Exception in thread \"" + thread.getName() + "\" ");
            ex.printStackTrace(br);
            br.flush();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
