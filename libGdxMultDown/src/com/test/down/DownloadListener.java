package com.test.down;

public interface DownloadListener {
    void downFinish();
    void error();
    void process(long all,long process);

}
