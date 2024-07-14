package com.libGdx.test.roll;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class RollApp extends LibGdxTestMain {
    public static void main(String[] args) {
        RollApp app = new RollApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        RollGroup rollGroup = new RollGroup();
        addActor(rollGroup);
        rollGroup.setPosition(
                Constant.GAMEWIDTH/2.0f,
                Constant.GAMEHIGHT/2.0f,
                Align.center);
    }
}
