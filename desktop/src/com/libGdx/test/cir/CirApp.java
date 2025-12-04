package com.libGdx.test.cir;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class CirApp extends LibGdxTestMain {
    public static void main(String[] args) {
        CirApp app = new CirApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        CirActor actor = new CirActor();
        addActor(actor);
        actor.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f, Align.center);
    }
}
