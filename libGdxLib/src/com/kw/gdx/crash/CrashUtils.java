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
                writeToFile(thread,throwable,"crash/");
            }
        });
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
        System.out.println(fname);
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
