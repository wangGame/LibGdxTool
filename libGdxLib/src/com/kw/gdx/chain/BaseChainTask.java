package com.kw.gdx.chain;

/**
 * 主要目的是完成一系列动作   继承此类，然后实现相应方法
 */
/**
 * Chain中的一个任务
 */
public interface BaseChainTask {

    /**
     * 开始执行当前Task
     */
    void runTask();

    /**
     * 当前Task执行完成
     */
    void finish();

    /**
     * 设置ChainManager
     */
    void setChainManager(ChainManager chainManager);
}
