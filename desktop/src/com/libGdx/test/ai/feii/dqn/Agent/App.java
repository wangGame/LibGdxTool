package com.libGdx.test.ai.feii.dqn.Agent;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.libGdx.test.ai.labyrinth.BoardGroup;
import com.libGdx.test.ai.labyrinth.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    private DemoDQN demoDQN;
    public static void main(String[] args) {
      App app = new App();
      app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        demoDQN = new DemoDQN(0.001f,0.995f);
        Constant.succesTimes = 0;
        BoardGroup boardGroup = demoDQN.getBoardGroup();
        addActor(boardGroup);
//        train(stage);
        test(stage);
    }


    private void train(Stage stage) {
        learning(stage);
    }

    private void test(Stage stage) {
        demoDQN.loadSnakeDQN("migongmodel.model");
        showPath(stage);
    }

    private void showPath(Stage stage) {
        demoDQN.reset();
        drawPath(stage);
    }

    private int times = 0;
    public void learning(Stage stage){
        if (Constant.succesTimes>Constant.learnTimes){

            demoDQN.saveSnakeDQN("migongmodel.model");
            return;
        }
        if (demoDQN.isSuccess()){
            Constant.succesTimes++;
        }else {
            Constant.failedTimes++;
        }

        System.out.println("-start times--" + Constant.succesTimes+"--success parent--"+Constant.succesTimes*1.0f/(Constant.succesTimes+Constant.failedTimes));
        demoDQN.reset();
        if (demoDQN.isDone()){
            stage.addAction(Actions.delay(0.05f,Actions.run(()->{
                train(stage);
            })));
        }else {
            re(stage);
        }
    }


    private void drawPath(Stage stage) {
        if (demoDQN.isDone())return;
        stage.addAction(Actions.delay(1,Actions.run(()->{
            demoDQN.stepBest();
            drawPath(stage);
        })));
    }

    public void re(Stage stage){
        stage.addAction(Actions.delay(0.05f,Actions.run(()->{
            demoDQN.step();
            if (!demoDQN.isDone()) {
                re(stage);
            }else {
                train(stage);
            }
        })));
    }
}
