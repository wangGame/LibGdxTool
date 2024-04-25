package com.libGdx.test.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.action.NumAction;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/20 10:17
 *
 * NumAction使用
 */
public class NumActionTest extends LibGdxTestMain {
    private Thread thread;
    private boolean xx = true;
    public static void main(String[] args) {
        NumActionTest test  = new NumActionTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        NumAction numAction = new NumAction(0, 100);
        numAction.setDuration(10);
        numAction.setReverse(true); //逆向
        numAction.setUpdateRunnable(new Runnable() {
            @Override
            public void run() {

            }
        });
        Image i = new Image(){
            @Override
            public void act(float delta) {
                super.act(delta);
//                System.out.println((int)numAction.getValue());
            }
        };
        i.addAction(numAction);
        stage.addActor(i);


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (xx){
                    try {
                        Thread.sleep(1000);
                        System.out.println("----thread----------");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        stage.addAction(Actions.sequence(
                Actions.delay(2),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        float v = 1/0;
                    }
                })
        ));

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                xx = false;
                throwable.printStackTrace();
            }
        });
    }

}
