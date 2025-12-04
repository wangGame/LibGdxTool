package com.libGdx.test.light;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class LightApp extends LibGdxTestMain {
    public static void main(String[] args) {
        LightApp app = new LightApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Lightgroup lightgroup = new Lightgroup();
        addActor(lightgroup);
        lightgroup.setOrigin(Align.center);
        lightgroup.setScale(0.3f);
        lightgroup.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f,Align.center);
    }
}
