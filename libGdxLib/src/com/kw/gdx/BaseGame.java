package com.kw.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.CpuPolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.anr.ANRError;
import com.kw.gdx.anr.ANRListener;
import com.kw.gdx.anr.ANRDEMO;
import com.kw.gdx.anr.ANRWatchDog;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.crash.CrashUtils;
import com.kw.gdx.resource.annotation.AnnotationInfo;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.utils.log.NLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BaseGame extends Game {
    private Batch batch;
    private Viewport stageViewport;
    protected ANRWatchDog dog;

    @Override
    public void create() {
        printInfo();
        gameInfoConfig();
        anrTest();
        initInstance();
        initViewport();
        initExtends();
        Gdx.app.postRunnable(()->{
            if (Constant.crashlog){
                Constant.SDPATH = Gdx.files.local("/").file().getAbsolutePath();
                new CrashUtils();
            }
            loadingView();
        });
    }

    private void anrTest() {
        ANRDEMO anrdemo = AnnotationInfo.checkClassAnnotation(this, ANRDEMO.class);
        if (anrdemo!=null){
            float delaytime = anrdemo.delaytime();
            dog = new ANRWatchDog((int) (delaytime ));
            dog.start();
            dog.setANRListener(new ANRListener() {
                @Override
                public void onAppNotResponding(ANRError error) {
                    error.printStackTrace();
                }
            });
        }
    }

    public static void setText(String start) {

    }

    private void printInfo() {
        String version = Gdx.gl.glGetString(GL20.GL_VERSION);
        String glslVersion = Gdx.gl.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
        NLog.i("version: %s ,glslVersion : %s",version,glslVersion);
        NLog.i("tool version: %s",Version.VERSION);
    }

    private void initExtends() {
        Asset.getAsset();
    }

    private void gameInfoConfig() {
        GameInfo info = AnnotationInfo.checkClassAnnotation(this,GameInfo.class);
        Constant.updateInfo(info);
    }

    protected void loadingView(){}

    private void initInstance(){
        Gdx.input.setCatchBackKey(true);
    }

    private void initViewport() {
        if (Constant.viewportType == Constant.EXTENDVIEWPORT) {
            stageViewport = new ExtendViewport(Constant.WIDTH, Constant.HIGHT);
        }else if (Constant.viewportType == Constant.FITVIEWPORT){
            stageViewport = new FitViewport(Constant.WIDTH, Constant.HIGHT);
        }else if (Constant.viewportType == Constant.STRETCHVIEWPORT){
            stageViewport = new StretchViewport(Constant.WIDTH, Constant.HIGHT);
        }else if (Constant.viewportType == Constant.FILLVIEWPORT){
            stageViewport = new FillViewport(Constant.WIDTH,Constant.WIDTH);
        }else if (Constant.viewportType == Constant.SCALINGVIEWPORTX){
            stageViewport = new ScalingViewport(Scaling.fillX,Constant.WIDTH,Constant.HIGHT);
        }else if (Constant.viewportType == Constant.SCALINGVIEWPORTY){
            stageViewport = new ScalingViewport(Scaling.fillY,Constant.WIDTH,Constant.HIGHT);
        }else if (Constant.viewportType == Constant.SCREENVIEWPORT){
            stageViewport = new ScreenViewport();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        viewPortResize(width, height);
    }

    private void viewPortResize(int width, int height) {
        stageViewport.update(width,height);
        Constant.updateSize(stageViewport);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(Constant.viewColor.r,Constant.viewColor.g,Constant.viewColor.b,Constant.viewColor.a);
        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
                | GL20.GL_DEPTH_BUFFER_BIT
                | GL20.GL_STENCIL_BUFFER_BIT);
        if (Constant.SHOWFRAMESPERSECOND){
            NLog.i("FramesPerSecond",Gdx.app.getGraphics().getFramesPerSecond());
        }
        super.render();
        if (Constant.SHOWRENDERCALL) {
            if (batch instanceof CpuPolygonSpriteBatch){
                System.out.println(((CpuPolygonSpriteBatch) (batch)).renderCalls);
            }
        }
    }

    public Viewport getStageViewport() {
        return stageViewport;
    }

    public Batch getBatch() {
        if (batch==null) {
            if (Constant.batchType == Constant.COUPOLYGONBATCH) {
                batch = new CpuPolygonSpriteBatch();
            }else if (Constant.batchType == Constant.SPRITEBATCH){
                batch = new SpriteBatch();
            }else {
                batch = new CpuPolygonSpriteBatch();
            }
        }
        return batch;
    }

    @Override
    public void dispose() {
        super.dispose();
        preDiapose();
        if (batch!=null) {
            batch.dispose();
            batch = null;
        }
        otherDispose();
    }


    public void setScreen(Class<? extends BaseScreen> t) {
        Constructor<?> constructor = t.getConstructors()[0];
        try {
            BaseScreen baseScreen = (BaseScreen) constructor.newInstance(this);
            setScreen(baseScreen);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setScreen(Screen screen) {
        if (screen instanceof BaseScreen) {
            Constant.currentActiveScreen = (BaseScreen) screen;
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    BaseGame.super.setScreen(screen);
                }
            });
        }else {
            BaseGame.super.setScreen(screen);
        }


    }

    protected void preDiapose(){

    }

    protected void otherDispose(){

    }
}
