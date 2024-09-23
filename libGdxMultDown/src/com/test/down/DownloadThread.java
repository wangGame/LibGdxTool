package com.test.down;

import com.test.down.http.HttpUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public  class DownloadThread  extends Thread {
    private File saveFile;
    private URL downUrl;
    private long block;
    private int threadId = -1;
    private int downLength;
    private boolean finish =  false;
    private FileDownloader downloader;

    public DownloadThread(FileDownloader downloader, URL downUrl, File saveFile,  long block,  int downLength,  int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.downloader = downloader;
        this.threadId = threadId;
        this.downLength = downLength;
    }

    @Override
    public  void run() {
        if(downLength < block){ // 未下载完成
            try {
                HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();
                http.setConnectTimeout(5 * 1000);
                http.setRequestMethod("GET");
                http.setRequestProperty("Referer", downUrl.toString());
                long startPos = block * (threadId - 1) + downLength; // 开始位置
                long endPos = block * threadId -1; // 结束位置
                http.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos); // 设置获取实体数据的范围
                HttpUtils.setHeader(http);
                InputStream inStream = http.getInputStream();
                byte[] buffer =  new  byte[1024];
                int offset = 0;
                RandomAccessFile threadfile =  new RandomAccessFile( this.saveFile, "rwd");
                threadfile.seek(startPos);
                while ((offset = inStream.read(buffer, 0, 1024)) != -1) {
                    threadfile.write(buffer, 0, offset);
                    downLength += offset;
                    downloader.update( this.threadId, downLength);
                    downloader.append(offset);
                }
                threadfile.close();
                inStream.close();
                this.finish =  true;
            }  catch (Exception e) {
                this.downLength = -1;
            }
        }
    }

    private  static  void print(String msg){

    }

    /**
     * 下载是否完成
     *  @return
     */
    public  boolean isFinish() {
        return finish;
    }

    /**
     * 已经下载的内容大小
     *  @return  如果返回值为-1,代表下载失败
     */
    public  long getDownLength() {
        return downLength;
    }
}