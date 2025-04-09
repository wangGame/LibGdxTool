package com.kw.gdx.chain;

public abstract class ChainTask implements BaseChainTask{
    protected BaseChainTask nextTask;
    @Override
    public void runTask() {

    }

    @Override
    public void nextTask() {
        if (nextTask!=null){
            nextTask.runTask();
        }
        finish();
    }

    @Override
    public void setNextTask(BaseChainTask nextTask) {
        this.nextTask = nextTask;
    }
}
