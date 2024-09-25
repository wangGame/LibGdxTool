package com.test.down;

import com.kw.gdx.file.JsonUtils;
import com.test.down.http.DefaultHttpClient;
import com.test.down.http.HttpClient;
import com.test.down.http.HttpUtils;
import com.test.down.status.DownLoadStatus;
import com.test.down.stream.FileDownloadRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class SplitTask extends Thread{
    private String downLoadString;
    private long startPosition;
    private long downLoadLength;
    private HttpClient client;
    private static final int BUFFER_SIZE = 1024 * 1024 * 4;
    private DownLoadStatus downloadStatus;
    private File downloadfile;

    public SplitTask(String downLoadString, long startPosition, long downLoadLength, String out){
        this.downLoadLength = downLoadLength;
        this.startPosition = startPosition;
        this.downLoadString = downLoadString;
        downloadfile = new File(out);
        this.downloadStatus = DownLoadStatus.READY;
    }

    @Override
    public void run() {
        super.run();
        this.downloadStatus = DownLoadStatus.DOWNING;
        System.out.println(Thread.currentThread()+" \n "+startPosition+"  "+(downLoadLength-1));
        client = new DefaultHttpClient();
        HttpURLConnection connect = null;
        try {
            long startPos = startPosition; // 开始位置
            long endPos = startPosition + downLoadLength - 1; // 结束位置
            connect = client.createConnect(downLoadString);
            connect.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos);
            client.connect();
            connect = HttpUtils.redirect(connect);
            long contentLengthLong = connect.getContentLengthLong();
            System.out.println(contentLengthLong);
            FileDownloadRandomAccessFile randomAccessFile = new FileDownloadRandomAccessFile(downloadfile);
            randomAccessFile.seek(startPosition);
            InputStream inputStream = connect.getInputStream();
            byte[] buff = new byte[BUFFER_SIZE];
            do {
                final int byteCount = inputStream.read(buff, 0, BUFFER_SIZE);
                if (byteCount == -1) {
                    downloadStatus = DownLoadStatus.FINISH;
                    break;
                }
                randomAccessFile.write(buff, 0, byteCount);
                randomAccessFile.flushAndSync();
            }while (true);
        } catch (IOException e) {
            this.downloadStatus = DownLoadStatus.FAILED;
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            this.downloadStatus = DownLoadStatus.FAILED;
            throw new RuntimeException(e);
        }
    }

    public DownLoadStatus getDownloadStatus() {
        return downloadStatus;
    }
}
