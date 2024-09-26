package com.test.down.bean;

import java.io.File;

public class DownLoadInfo {
    private String url;
    private int threadNum;
    private String savePath;
    private long contentLengthLong;
    private long startPosition;
    private long endPosition;
    private long currentPosition;

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public void setFilePath(String savePath) {
        this.savePath = savePath;
    }

    public void setContentLength(long contentLengthLong) {
        this.contentLengthLong = contentLengthLong;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getContentLengthLong() {
        return contentLengthLong;
    }

    public void setContentLengthLong(long contentLengthLong) {
        this.contentLengthLong = contentLengthLong;
    }
}
