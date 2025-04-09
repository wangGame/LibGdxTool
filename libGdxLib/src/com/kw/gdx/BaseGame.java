package com.kw.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
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
    private Screen zhuanCScreen;
    private Batch batch;
    protected Viewport stageViewport;
    protected ANRWatchDog dog;

    @Override
    public void create() {
        printInfo();
        gameInfoConfig();
        anrTest();
        initInstance();
        initViewport();
        initExtends();
        initScreen();
    }

    protected void initScreen() {
        Gdx.app.postRunnable(()->{
            if (Constant.crashlog){
                Constant.SDPATH = Gdx.files.local("/").file().getAbsolutePath();
                new CrashUtils();
            }
            loadingView();
        });
    }

    protected void anrTest() {
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

    protected void printInfo() {
        String version = Gdx.gl.glGetString(GL20.GL_VERSION);
        String glslVersion = Gdx.gl.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
        NLog.i("version: %s ,glslVersion : %s",version,glslVersion);
        NLog.i("LibgdxTool version: %s",Version.VERSION);
    }

    private void initExtends() {
        Asset.getAsset();
    }

    protected void gameInfoConfig() {
        GameInfo info = AnnotationInfo.checkClassAnnotation(this,GameInfo.class);
        Constant.updateInfo(info);
    }

    protected void loadingView(){}

    protected void initInstance(){
        Gdx.input.setCatchKey(Input.Keys.BUTTON_CIRCLE,true);
    }

    protected void initViewport() {
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
        viewPortResize(width, height);
        super.resize(width,height);
        if (zhuanCScreen!=null){
            zhuanCScreen.resize(width, height);
        }
    }

    private void viewPortResize(int width, int height) {
        stageViewport.update(width,height,true);
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
            NLog.i("FramesPerSecond %s",Gdx.app.getGraphics().getFramesPerSecond());
        }
        super.render();
        if (Constant.SHOWRENDERCALL) {
            if (batch instanceof com.esotericsoftware.spine.utils.TwoColorPolygonBatch){
//                System.out.println(((TwoColorPolygonBatch) (batch)).renderCalls);
            }
        }
        if (zhuanCScreen!=null){
            zhuanCScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    public Viewport getStageViewport() {
        return stageViewport;
    }

    public Batch getBatch() {
        if (batch==null) {
//            if (Constant.batchType == Constant.COUPOLYGONBATCH) {
//                batch = new CpuPolygonSpriteBatch();
//            }else if (Constant.batchType == Constant.SPRITEBATCH){
//                batch = new SpriteBatch();
//            }else {
//                batch = new CpuPolygonSpriteBatch();
//            }
            batch = new TwoColorPolygonBatch();
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
        if (zhuanCScreen!=null){
            zhuanCScreen.dispose();
        }
        otherDispose();
    }

    public void setScreen(Class<? extends BaseScreen> t) {
        setScreen(t,false);
    }

    public void setScreen(Class<? extends BaseScreen> t,boolean isGc) {
        Constructor<?> constructor = t.getConstructors()[0];
        try {
            BaseScreen baseScreen = (BaseScreen) constructor.newInstance(this);
            if (isGc) {
                zhuanCScreen = baseScreen;
                zhuanCScreen.show();
            }else {
                setScreen(baseScreen);
            }
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
        setScreen(screen,false);
    }

    public void setScreen(Screen screen,boolean isGuc) {
        if (!isGuc) {
            if (screen instanceof BaseScreen) {
                Constant.currentActiveScreen = (BaseScreen) screen;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        BaseGame.super.setScreen(screen);
                    }
                });
            } else {
                BaseGame.super.setScreen(screen);
            }
        }else {
            zhuanCScreen = screen;
            zhuanCScreen.show();
        }
    }

    protected void preDiapose(){

    }

    protected void otherDispose(){

    }


    @Override
    public void pause () {
        super.pause();
        if (zhuanCScreen!=null) {
            zhuanCScreen.pause();
        }
    }

    @Override
    public void resume () {
        super.resume();
        if (zhuanCScreen!=null){
            zhuanCScreen.resume();
        }
    }


    public void zhuancEnd() {
        if (zhuanCScreen!=null) {
            zhuanCScreen.hide();
            zhuanCScreen.dispose();
            zhuanCScreen = null;
        }
    }
}
