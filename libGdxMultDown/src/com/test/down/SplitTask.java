package com.test.down;

import com.kw.gdx.file.JsonUtils;
import com.test.down.bean.DownLoadInfo;
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
    private int blockNum = 0;
    private String tempPath;
    private DownLoadInfo downLoadInfo;
    public SplitTask(String downLoadString, long startPosition, long downLoadLength, String out, int i, String outFileTemp){
        this.downLoadLength = downLoadLength;
        this.startPosition = startPosition;
        this.downLoadString = downLoadString;
        downloadfile = new File(out);
        this.downloadStatus = DownLoadStatus.READY;
        this.blockNum = i;
        this.tempPath = outFileTemp;
    }

    @Override
    public void run() {
        super.run();
        this.downloadStatus = DownLoadStatus.DOWNING;
        System.out.println(Thread.currentThread()+" \n "+startPosition+"  "+(downLoadLength-1));
        client = new DefaultHttpClient();
        HttpURLConnection connect = null;
        try {
            long endPos = startPosition + downLoadLength - 1; // 结束位置
            connect = client.createConnect(downLoadString);

            this.downLoadInfo = JsonUtils.read(tempPath + "-" + blockNum, DownLoadInfo.class);
            if (downLoadInfo!=null){
                long oldStartPosition = downLoadInfo.getStartPosition();
                long oldEndPosition = downLoadInfo.getEndPosition();
                long currentPosition = downLoadInfo.getCurrentPosition();
                if (oldStartPosition == startPosition && oldEndPosition == endPos) {
                    startPosition = startPosition + currentPosition-1;
                }else {
                    downLoadInfo.setStartPosition(startPosition);
                    downLoadInfo.setEndPosition(endPos);
                    downLoadInfo.setContentLength(0);
                }
            }else {
                downLoadInfo = new DownLoadInfo();
                downLoadInfo.setStartPosition(startPosition);
                downLoadInfo.setEndPosition(endPos);
                downLoadInfo.setContentLength(0);
            }


            connect.setRequestProperty("Range", "bytes=" + startPosition + "-"+ endPos);
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
                System.out.println(downLoadInfo.getCurrentPosition());
                if (byteCount == -1) {
                    downloadStatus = DownLoadStatus.FINISH;
                    JsonUtils.delete(tempPath + "-" + blockNum);
                    break;
                }
                randomAccessFile.write(buff, 0, byteCount);
                randomAccessFile.flushAndSync();
                downLoadInfo.setCurrentPosition(downLoadInfo.getCurrentPosition()+byteCount);
                JsonUtils.save(tempPath + "-" + blockNum, downLoadInfo);
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

    public DownLoadInfo getDownLoadInfo() {
        return downLoadInfo;
    }
}
