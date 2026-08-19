package com.test.down.task;

import com.test.down.listener.DownloadListener;
import com.test.down.utils.Md5;

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
        StringBuilder sb = new StringBuilder();
        sb.append(downloadUrl);
        sb.append(saveDir);
        sb.append(saveFile);
        String md5 = Md5.getMd5(sb.toString());
        if (taskHashMap.containsKey(md5)){
            System.out.println("已经存在，正在下载中"+downloadUrl);
            return;
        }
        System.out.println("dow=-============?????????");
        DownLoadTask task = new DownLoadTask(md5);
        taskHashMap.put(task.getTaskId(),task);

        try {
            task.down(downloadUrl,saveDir,saveFile);
            task.addListener(new DownloadListener() {
                @Override
                public void downFinish() {
                    System.out.println("finish ====>");
                    taskHashMap.remove(task.getTaskId());
                }

                @Override
                public void error() {
                    taskHashMap.remove(task.getTaskId());
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

    public static void closeDownload(){
        fileManager = null;
    }
}
