package com.kw.gdx;

import com.anrutils.example.ANRError;
import com.anrutils.example.ANRWatchDog;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.CpuPolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.AnnotationInfo;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.utils.log.NLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class BaseGame extends Game {
    private Batch batch;
    private Viewport stageViewport;
    private ANRWatchDog anrWatchDog;

    @Override
    public void create() {
        printInfo();
        gameInfoConfig();
        initInstance();
        initViewport();
        initExtends();
        if (Constant.watchDog){
            initAnrWatchDog();
        }
        Gdx.app.postRunnable(()->{
            loadingView();
        });
    }

    public static void setText(String start) {

    }

    private void printInfo() {
        String version = Gdx.gl.glGetString(GL20.GL_VERSION);
        String glslVersion = Gdx.gl.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
        NLog.i("version: %s ,glslVersion : %s",version,glslVersion);
    }

    private void initExtends() {
//        extendInfo = new Label("1111111111111111111111111111111",new Label.LabelStyle(){{
//            font =Asset.getAsset().loadBitFont("font/Bahnschrift-Regular_40_1.fnt");
//        }});
        Asset.getAsset();
    }

    public void initAnrWatchDog(){
        anrWatchDog = new ANRWatchDog(Constant.watchDogTime);
        anrWatchDog.setANRListener(new ANRWatchDog.ANRListener() {
                    @Override
                    public void onAppNotResponding(ANRError error) {
                        NLog.e("ANR-Watchdog-Demo", "Detected Application Not Responding!");

                        // Some tools like ACRA are serializing the exception, so we must make sure the exception serializes correctly
                        try {
                            new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(error);
                        }
                        catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        NLog.i("ANR-Watchdog-Demo", "Error was successfully serialized");
                        try {
                            throw error;
                        } catch (ANRError anrError) {
                            anrError.printStackTrace();
                        }
                    }
                })
                .setANRInterceptor(new ANRWatchDog.ANRInterceptor() {
                    @Override
                    public long intercept(long duration) {
                        long ret = 4 * 1000 - duration;
                        if (ret > 0)
                            NLog.i("ANR-Watchdog-Demo", "Intercepted ANR that is too short (" + duration + " ms), postponing for " + ret + " ms.");
                        return ret;
                    }
                })
        ;
        anrWatchDog.setReportThreadNamePrefix("|ANR-WatchDog|");
        anrWatchDog.start();
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
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
