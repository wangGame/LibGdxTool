package com.libGdx.test.task;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.chain.ChainManager;
import com.libGdx.test.base.LibGdxTestMain;

public class TaskDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        TaskDemo taskDemo = new TaskDemo();
        taskDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ChainManager chainManager = new ChainManager();
        TaskOne taskOne1 = new TaskOne();
        TaskOne taskOne2 = new TaskOne();
        TaskOne taskOne3 = new TaskOne();
        TaskOne taskOne4 = new TaskOne();
        chainManager.addTask(taskOne1);
        chainManager.addTask(taskOne2);
        chainManager.addTask(taskOne3);
        chainManager.addTask(taskOne4);
        chainManager.execute();

        addActor(taskOne1);
        addActor(taskOne2);
        addActor(taskOne3);
        addActor(taskOne4);

        chainManager.endRunnable(()->{
            System.out.println("end ====> ");
        });

    }
}
