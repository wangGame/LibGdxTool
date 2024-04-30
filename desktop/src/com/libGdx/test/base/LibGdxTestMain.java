package com.libGdx.test.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.BaseGame;
import com.kw.gdx.anr.ANRDEMO;
import com.kw.gdx.screen.BaseScreen;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/21 15:51
 */
@ANRDEMO
public class LibGdxTestMain extends BaseGame {

    private Stage stageMain;

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new TestScreen(this));
    }

    class TestScreen extends BaseScreen {
        public TestScreen(BaseGame game) {
            super(game);
            stageMain = stage;
        }

        @Override
        public void show() {
            super.show();
            useShow(stage);
        }
    }

    public void useShow(Stage stage) {

    }

    public void addActor(Actor actor){
        stageMain.addActor(actor);
    }

    public void start() {
        start(this);
    }

    public void start(LibGdxTestMain test) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1920 * 0.25f);
        config.width = (int) (1080 * 0.3f);
        Gdx.isJiami = true;
        new LwjglApplication(test, config);
    }
}
