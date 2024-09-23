package com.test.down;

import java.io.File;

public class App {
    String url = "http://192.168.1.192/installapk.7z";
//    String url = "http://192.168.1.192/teet.7z";

    private  void download( final String path,  final File savedir,int num) {
        FileDownloader loader =  new FileDownloader(path, savedir, num);
        try {
            loader.download();
        }  catch (Exception e) {
            e.printStackTrace();
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
