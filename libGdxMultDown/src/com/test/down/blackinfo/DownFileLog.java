package com.test.down.blackinfo;

/**
 * 存储每部分的位置
 *
 *
 */
public class DownFileLog {
    private String downpath;
    private int threadid;
    private int downlength;

    public String getDownpath() {
        return downpath;
    }

    public void setDownpath(String downpath) {
        this.downpath = downpath;
    }

    public int getThreadid() {
        return threadid;
    }

    public void setThreadid(int threadid) {
        this.threadid = threadid;
    }

    public int getDownlength() {
        return downlength;
    }

    public void setDownlength(int downlength) {
        this.downlength = downlength;
    }

    @Override
    public String toString() {
        return "DownFileLog{" +
                "downpath='" + downpath + '\'' +
                ", threadid=" + threadid +
                ", downlength=" + downlength +
                '}';
    }
}
