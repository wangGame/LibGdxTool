package com.test.down;

import com.test.down.listener.DownloadListener;
import com.test.down.task.DownFileManager;
import com.test.down.task.DownLoadTask;

import java.io.File;
import java.io.IOException;

public class App {
    String basePath = "D:/img";

    public static void main(String[] args) {
        String strUrl[] = {
                "video1.f4v",
                "video2.f4v",
                "video1.f4v",
                "video2.f4v",
                "video1.f4v",
                "video2.f4v"
        };
        App app = new App();
        DownFileManager instance = DownFileManager.getInstance();
        for (String s : strUrl) {
            String downLoadUrl = "http://192.168.1.59/ceshi/"+s;
            instance.download(downLoadUrl, app.basePath, s);
        }
    }
}
