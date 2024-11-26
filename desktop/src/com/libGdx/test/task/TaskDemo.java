package com.libGdx.test.task;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.chain.BaseChainTask;
import com.kw.gdx.chain.ChainTask;
import com.libGdx.test.base.LibGdxTestMain;

public class TaskDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        TaskDemo taskDemo = new TaskDemo();
        taskDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        TaskOne taskOne = new TaskOne();
        TaskTwo taskTwo = new TaskTwo();
        taskOne.setNextTask(taskTwo);
    }
}
