package com.libGdx.test.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kw.gdx.BaseGame;
import com.kw.gdx.anr.ANRDEMO;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.BaseScreen;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/21 15:51
 */
@ANRDEMO
@GameInfo(width = 1080,height = 1920)
public class LibGdxTestMain extends BaseGame {
    protected static float screenWidth = 1080;
    protected static float screenHight = 1920;
    private Stage stageMain;

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new TestScreen(this));
    }


    public static void run(Class<? extends LibGdxTestMain> c){
        try{
            LibGdxTestMain libGdxTestMain = c.getDeclaredConstructor().newInstance();
            libGdxTestMain.start();
        }catch (Exception e){

        }
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
        config.height = (int) (screenHight * 0.5f);
        config.width = (int) (screenWidth * 0.5f);
        new LwjglApplication(test, config);
    }

    @Override
    protected void initViewport() {
        stageViewport = new ExtendViewport(1080,1920);
    }
}
