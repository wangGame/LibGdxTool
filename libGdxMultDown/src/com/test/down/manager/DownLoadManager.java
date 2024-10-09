package com.test.down.manager;

import com.badlogic.gdx.utils.Queue;
import com.test.down.task.DownLoadTask;

public class DownLoadManager {
    private Queue<DownLoadTask> downLoadTasks;
    private static DownLoadManager downLoadManager;
    public static DownLoadManager getInstance(){
        if (downLoadManager == null) {
            downLoadManager = new DownLoadManager();
        }
        return downLoadManager;
    }

    public void dispose(){
        downLoadManager = null;
    }
}
