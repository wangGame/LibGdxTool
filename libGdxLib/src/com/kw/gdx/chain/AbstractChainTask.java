package com.kw.gdx.chain;

public abstract class AbstractChainTask implements BaseChainTask {

    protected ChainManager chainManager;

    @Override
    public void setChainManager(ChainManager chainManager) {
        this.chainManager = chainManager;
    }

    @Override
    public void finish() {

        if (chainManager != null) {
            chainManager.taskFinish();
        }
    }
}