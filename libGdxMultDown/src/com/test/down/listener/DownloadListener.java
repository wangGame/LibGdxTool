package com.test.down.listener;

public interface DownloadListener {
    void downFinish();
    void error();
    void process(long all,long process);

}
