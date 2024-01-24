package com.libGdx.test.ai.dqn.Agent;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.libGdx.test.ai.path.BoardGroup;
import com.libGdx.test.ai.path.Constant;
import com.libGdx.test.ai.path.PathState;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
      App app = new App();
      app.start();
    }

    private DemoDQN demoDQN;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        demoDQN = new DemoDQN(0.001f,0.995f);
        Constant.succesTimes = 0;
        updateExce(stage);
        BoardGroup boardGroup = demoDQN.getBoardGroup();
        addActor(boardGroup);
    }

    private int times = 0;
    public void updateExce(Stage stage){
        if (times>Constant.learnTimes){
            System.out.println(Constant.succesTimes * 1.0f / times);
            showPath(stage);
            demoDQN.saveSnakeDQN(demoDQN,"migongmodel.model");
            return;
        }
        times ++;
        System.out.println("-----start times--------" + times);
        demoDQN.reset();
        if (demoDQN.isDone()){
            stage.addAction(Actions.delay(0.1f,Actions.run(()->{
                updateExce(stage);
            })));
        }else {
            re(stage);
        }
    }

    private void showPath(Stage stage) {
        demoDQN.reset();
        drawPath(stage);
    }

    private void drawPath(Stage stage) {
        if (demoDQN.isDone())return;
        stage.addAction(Actions.delay(1,Actions.run(()->{
            demoDQN.stepBest();
            drawPath(stage);
        })));
    }

    public void re(Stage stage){
        stage.addAction(Actions.delay(0.1f,Actions.run(()->{
            demoDQN.step();
            if (!demoDQN.isDone()) {
                re(stage);
            }else {
                updateExce(stage);
            }
        })));
    }
}
