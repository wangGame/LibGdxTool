package com.libGdx.test.dfs;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DfsTest extends LibGdxTestMain {
    Utils utils = new Utils();

    public static void main(String[] args) {
        DfsTest test = new DfsTest();
        test.start();
    }
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        LinkedList<Integer> track = new LinkedList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                utils.br(new int[]{1, 2, 3, 4}, track);
            }
        }).start();
        stage.addAction(Actions.forever(Actions.delay(0.1f,Actions.run(()->{
            utils.notifyM();
        }))));
    }
}