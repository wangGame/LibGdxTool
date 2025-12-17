package com.test.down.task;

import com.test.down.listener.DownloadListener;

import java.io.IOException;
import java.util.HashMap;

public class DownFileManager {
    private HashMap<String,DownLoadTask> taskHashMap = new HashMap<>();
    private static DownFileManager fileManager;
    public static DownFileManager getInstance() {
        if (fileManager == null){
            fileManager = new DownFileManager();
        }
        return fileManager;
    }

    public void download(final String downloadUrl,
                           final String saveDir,
                           final String saveFile) {
        if (taskHashMap.containsKey(downloadUrl)){
            System.out.println("已经存在，正在下载中"+downloadUrl);
            return;
        }
        DownLoadTask task = new DownLoadTask();
        try {
            task.down(downloadUrl,saveDir,saveFile);
            task.addListener(new DownloadListener() {
                @Override
                public void downFinish() {
                    System.out.println("finish ====>");
                    taskHashMap.remove(task.getUrl());
                }

                @Override
                public void error() {
                    taskHashMap.remove(task.getUrl());
                }

                @Override
                public void process(long all, long process) {
                    System.out.println(all +"  == "+process+"  "+process/all);
                }
            });
            taskHashMap.put(downloadUrl,task);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
