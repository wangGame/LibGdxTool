package com.kw.gdx.chain;

/**
 * 主要目的是完成一系列动作   继承此类，然后实现相应方法
 */
public interface BaseChainTask {
    void runTask();
    void nextTask();
    void setNextTask(BaseChainTask nextTask);
    void finish();
}
