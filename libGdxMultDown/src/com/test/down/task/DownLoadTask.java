package com.test.down.task;

import com.badlogic.gdx.utils.Array;
import com.kw.gdx.file.JsonUtils;
import com.test.down.bean.DownLoadInfo;
import com.test.down.http.DefaultHttpClient;
import com.test.down.http.HttpClient;
import com.test.down.http.HttpUtils;
import com.test.down.listener.DownloadListener;
import com.test.down.status.DownLoadStatus;
import com.test.down.stream.FileDownloadRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

public class DownLoadTask {
    private HttpClient client;
    private int threadNum = 3;
    private Array<SplitTask> downLoadThread;

    public void down(String url,String saveDir,String saveFile) throws IOException, IllegalAccessException {
        //存储目录
        String tempPath = saveDir+"/"+saveFile;
        this.downLoadThread = new Array<>();
        client = new DefaultHttpClient();
        HttpURLConnection connect = client.createConnect(url);
        connect.connect();
        connect = HttpUtils.redirect(connect);
        long contentLengthLong = connect.getContentLengthLong();
        System.out.println(contentLengthLong);
        //如果文件存在就删除
        File file = new File(tempPath);
        if (file.exists()){
//            file.delete();

        }
        FileDownloadRandomAccessFile randomAccessFile = new FileDownloadRandomAccessFile(file);
        randomAccessFile.setLength(contentLengthLong);
        long splitSizie = contentLengthLong / threadNum;
        long startpostion = 0;
        int uniqueId = HttpUtils.getUniqueId(url, saveDir, saveFile, "3");
        String outFileTemp = file.getAbsoluteFile().getParent() +"/"+uniqueId +"/"+
                "temp/partfile" + uniqueId;
        DownLoadInfo read = JsonUtils.read(outFileTemp, DownLoadInfo.class);
        if (read != null){
            //文件更改就删除
            if (read.getContentLengthLong() != contentLengthLong) {
                JsonUtils.delete(outFileTemp);
            }
        }
        DownLoadInfo downLoadInfo = new DownLoadInfo();
        downLoadInfo.setUrl(url);
        downLoadInfo.setThreadNum(threadNum);
        downLoadInfo.setFilePath(file.getAbsoluteFile().getPath());
        downLoadInfo.setContentLength(contentLengthLong);
        JsonUtils.save(outFileTemp,downLoadInfo);
        int blackNum = 0;
        for (int i = 0; i < 2; i++) {
            SplitTask splitTask = new SplitTask(url, startpostion, splitSizie, tempPath, blackNum++, outFileTemp);
            downLoadThread.add(splitTask);
            startpostion += splitSizie;
        }
        SplitTask splitTask = new SplitTask(url, startpostion, contentLengthLong - startpostion, tempPath, blackNum++, outFileTemp);
        downLoadThread.add(splitTask);

        for (Thread thread1 : downLoadThread) {
            thread1.start();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    boolean finish = true;
                    long allCount = 0;
                    for (SplitTask task : downLoadThread) {
                        if (task.getDownloadStatus() != DownLoadStatus.FINISH) {
                            finish = false;
                        }
                        allCount += task.getDownLoadInfo().getCurrentPosition();
                    }
                    downloadListener.process(contentLengthLong, allCount);
                    if (finish) {
                        downloadListener.downFinish();

                        String outFileTemp1 = file.getAbsoluteFile().getParent() +"/"+uniqueId ;
                        File file1 = new File(outFileTemp1);
                        deleteDirectory(file1);
                        break;
                    }
                }
            }
        }).start();
    }

    // 递归删除文件夹及其内容
    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file); // 递归删除子目录
                }
                file.delete(); // 删除文件
            }
        }
        directory.delete(); // 最后删除空目录
    }

    private DownloadListener downloadListener;
    public void addListener(DownloadListener listener) {
        this.downloadListener = listener;
    }
}
