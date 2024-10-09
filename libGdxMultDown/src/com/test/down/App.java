package com.test.down;

import com.test.down.listener.DownloadListener;
import com.test.down.task.DownLoadTask;

import java.io.File;
import java.io.IOException;

public class App {
    String url = "http://192.168.1.192/ceshi/android-debug.apk";
//    String url = "http://192.168.1.192/teet.7z";

    private  void download(final String downloadUrl,
                           final String saveDir,
                           final String saveFile) {
        DownLoadTask task = new DownLoadTask();
        try {
            task.down(downloadUrl,saveDir,saveFile);
            task.addListener(new DownloadListener() {
                @Override
                public void downFinish() {
                    System.out.println("finish ====>");
                }

                @Override
                public void error() {

                }

                @Override
                public void process(long all, long process) {
                    System.out.println(all +"  == "+process+"  "+process/all);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        App app = new App();
        new Thread(new Runnable() {
            @Override
            public void run() {
                app.download(app.url, "","");
            }
        }).start();
    }
}
