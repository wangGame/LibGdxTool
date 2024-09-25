package com.test.down.bean;

import java.io.File;

public class DownLoadInfo {
    private String url;
    private int threadNum;
    private String savePath;

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
}
