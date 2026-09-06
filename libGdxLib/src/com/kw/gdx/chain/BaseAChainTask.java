package com.kw.gdx.chain;

/**
 * 普通组
 */
public abstract class BaseAChainTask implements BaseChainTask{
    private ChainManager chainManager;
    @Override
    public abstract void runTask() ;

    @Override
    public abstract void finish();

    @Override
    public void setChainManager(ChainManager chainManager) {
        this.chainManager = chainManager;
    }
}
