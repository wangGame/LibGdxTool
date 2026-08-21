package com.test.down;

import com.test.down.listener.DownloadListener;
import com.test.down.task.DownFileManager;
import com.test.down.task.DownLoadTask;

import java.io.File;
import java.io.IOException;

public class App {
    String basePath = "D:/img";

    public static void main(String[] args) {
        App app = new App();
        DownFileManager instance = DownFileManager.getInstance();
        for (int i = 0; i < 10; i++) {
            String downLoadUrl = "http://127.0.0.1/2.mp4";
            instance.download(downLoadUrl, app.basePath, "2.mp4");
        }
    }
}
