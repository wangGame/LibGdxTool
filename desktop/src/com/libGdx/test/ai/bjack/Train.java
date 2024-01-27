package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class Train extends LibGdxTestMain {
    public static void main(String[] args) {
        Train train = new Train();
        train.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        AppQLearning appQLearning = new AppQLearning();
//        appQLearning.startQLearning();
        appQLearning.loadModel("blackj.skl");
        addActor(appQLearning);
    }
}
