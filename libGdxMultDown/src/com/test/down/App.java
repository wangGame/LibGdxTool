package com.test.down;

import java.io.File;
import java.io.IOException;

public class App {
    String url = "http://192.168.1.192/ceshi/android-debug.apk";
//    String url = "http://192.168.1.192/teet.7z";

    private  void download( final String path,  final File savedir,int num) {
        DownLoadTask task = new DownLoadTask();
        try {
            task.down(url,"out.zip");
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
                app.download(app.url, new File("./"),4);
            }
        }).start();
    }
}
