package com.kw.gdx.chain;

import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * 用于Group的任务组
 */
public abstract class BaseChainTaskGroup extends Group implements BaseChainTask {
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
