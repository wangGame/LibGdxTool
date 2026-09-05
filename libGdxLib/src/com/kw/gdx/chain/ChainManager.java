package com.kw.gdx.chain;

import com.badlogic.gdx.utils.Array;



import com.badlogic.gdx.utils.Array;

public class ChainManager {

    /**
     * 所有Task
     */
    private final Array<BaseChainTask> chainTasks = new Array<>();

    /**
     * 当前执行到第几个
     */
    private int currentIndex = 0;

    /**
     * 完成回调
     */
    private Runnable endRunnable;

    /**
     * 是否正在执行
     */
    private boolean running = false;

    /**
     * 是否已经结束
     */
    private boolean finished = false;


    /**
     * 添加Task
     */
    public ChainManager addTask(BaseChainTask task) {

        if (task == null) {
            throw new IllegalArgumentException("task cannot be null");
        }

        chainTasks.add(task);

        task.setChainManager(this);

        return this;
    }


    /**
     * 开始执行
     */
    public void execute() {

        if (running) {
            return;
        }

        if (finished) {
            return;
        }

        running = true;

        executeNext();
    }


    /**
     * 执行下一个Task
     */
    private void executeNext() {

        // 所有Task执行完成
        if (currentIndex >= chainTasks.size) {

            running = false;
            finished = true;

            if (endRunnable != null) {
                endRunnable.run();
            }

            return;
        }


        BaseChainTask task = chainTasks.get(currentIndex);

        currentIndex++;

        task.runTask();
    }


    /**
     * 当前Task完成后调用
     */
    public void taskFinish() {

        if (!running) {
            return;
        }

        executeNext();
    }


    /**
     * 设置全部完成回调
     */
    public ChainManager endRunnable(Runnable runnable) {

        this.endRunnable = runnable;

        return this;
    }


    /**
     * 取消Chain
     */
    public void cancel() {

        running = false;
        finished = true;
    }


    /**
     * 是否正在执行
     */
    public boolean isRunning() {
        return running;
    }


    /**
     * 是否已经完成
     */
    public boolean isFinished() {
        return finished;
    }


    /**
     * 重置Chain
     *
     * 可以再次execute()
     */
    public void reset() {

        currentIndex = 0;

        running = false;

        finished = false;
    }


    /**
     * 清空所有Task
     */
    public void clear() {

        chainTasks.clear();

        reset();
    }


    /**
     * 获取当前Task索引
     */
    public int getCurrentIndex() {
        return currentIndex;
    }


    /**
     * 获取Task数量
     */
    public int getTaskCount() {
        return chainTasks.size;
    }
}