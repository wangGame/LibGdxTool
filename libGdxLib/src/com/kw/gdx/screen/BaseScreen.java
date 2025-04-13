package com.kw.gdx.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.BaseGame;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.AnnotationInfo;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.kw.gdx.utils.ads.BannerManager;
import com.kw.gdx.utils.ads.BannerView;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.view.dialog.DialogManager;
import com.kw.gdx.view.dialog.base.BaseDialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BaseScreen implements Screen {
    protected boolean dispose;
    protected final Stage stage;
    protected Group rootView;
    protected Group dialogView;
    protected Group maxTopGroup;
    protected String viewpath;
    protected float offsetLeft;
    protected float offsetRight;
    protected float offsetTop;
    protected float offsetBottom;
    //使用上下左右四个方位   如果存在挖孔，两个偏移存在问题。
//    建议使用offsetX
    @Deprecated
    protected float offsetY;
    @Deprecated
    protected float offsetX;
    protected BaseGame game;
    protected final DialogManager dialogManager;
    protected final BannerManager bannerManager;
    protected float centerX;
    protected float centerY;
    private InputMultiplexer multiplexer;
    protected float bannerHight;
    protected boolean activeScreen = false;
    protected Vector2 screenSize;

    public BaseScreen(BaseGame game){
        activeScreen = true;
        this.game = game;
        this.screenSize = new Vector2(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        this.stage = new Stage(getStageViewport(), getBatch());
        this.bannerManager = new BannerManager(stage);
        stage.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.R) {
                    r();
                }
                return super.keyUp(event, keycode);
            }
        });
        dialogView = new Group();
        this.dialogManager = new DialogManager(dialogView);
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(stage);
        uiResize();
    }

    private void uiResize() {
        if (Constant.viewportType == Constant.EXTENDVIEWPORT) {
            this.offsetY = (Constant.GAMEHIGHT - Constant.HIGHT) / 2;
            this.offsetX = (Constant.GAMEWIDTH - Constant.WIDTH) / 2;
        }else if (Constant.viewportType == Constant.SCALINGVIEWPORTX){
            this.offsetY = (Constant.GAMEHIGHT - Constant.HIGHT) / 2;
            this.offsetX = (Constant.GAMEWIDTH - Constant.WIDTH) / 2;
        }else if (Constant.viewportType == Constant.SCALINGVIEWPORTY){
            this.offsetY = (Constant.GAMEHIGHT - Constant.HIGHT) / 2;
            this.offsetX = (Constant.GAMEWIDTH - Constant.WIDTH) / 2;
        }
        this.bannerManager.init(offsetY);
        if (Constant.DEBUG) {
            this.bannerManager.setVisible(true);
        }
        this.centerX = Constant.GAMEWIDTH / 2;
        this.centerY = Constant.GAMEHIGHT / 2;

        bannerHight = BannerView.pxToDp(Configuration.bannerHeight);
//        stage.getRoot().setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2,Align.center);
        if (rootView!=null){
            rootView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        }
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            offsetTop = offsetY;
        }else {
            NLog.i("getSafeInsetTop size:"+Gdx.graphics.getSafeInsetTop());
            offsetTop = offsetY -  Gdx.graphics.getSafeInsetTop();
        }
        offsetBottom = offsetY;
        offsetLeft = offsetX;
        offsetRight = offsetX;

        Constant.offsetBottom = offsetBottom;
        Constant.offsetTop = offsetTop;
        Constant.offsetLeft = offsetLeft;
        Constant.offsetRight = offsetRight;
    }

    protected void r() {

    }

    protected void initAnnotation(){

    }

    public DialogManager getDialogManager(){
        return dialogManager;
    }

    private Batch getBatch() {
        return game.getBatch();
    }

    private Viewport getStageViewport() {
        return game.getStageViewport();
    }

    public void touchDisable(){
        Gdx.input.setInputProcessor(null);
    }

    public void touchEnable(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        initTouch();
        initRootView();
        initAnnotation();
        initData();
        initView();
    }

    protected void initData() {

    }

    public void initView(){}

    private void initRootView() {
        ScreenResource annotation = AnnotationInfo.checkClassAnnotation(this, ScreenResource.class);
        if (annotation!=null){
            viewpath = annotation.value();
            rootView = CocosResource.loadFile(viewpath);
            stage.addActor(rootView);
            rootView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        }else {
            rootView = new Group();
            stage.addActor(rootView);
            rootView.setSize(Constant.WIDTH,Constant.HIGHT);
            rootView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        }

        maxTopGroup = new Group();
        stage.addActor(dialogView);
        stage.addActor(maxTopGroup);
        dialogView.setSize(Constant.WIDTH,Constant.HIGHT);
        dialogView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        maxTopGroup.setSize(Constant.WIDTH,Constant.HIGHT);
        maxTopGroup.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        dialogView.setTouchable(Touchable.childrenOnly);
        maxTopGroup.setTouchable(Touchable.childrenOnly);
    }

    private void initTouch() {
        stage.addListener(BackInputListener());
        touchEnable();
    }

    private InputListener BackInputListener() {
        return new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK)) {
                    back();
                }
                return super.keyDown(event, keycode);
            }
        };
    }

    protected BaseDialog back() {
        BaseDialog back = dialogManager.back();
        return back;
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
//        banner.toFront();
        bannerManager.toFront();
//        o.toFront();
    }

    private float oldWidth;
    private float oldHeight;

    @Override
    public void resize(int width, int height) {
        if (oldWidth != Constant.GAMEWIDTH || oldHeight != Constant.GAMEHIGHT) {
            this.oldWidth = Constant.GAMEWIDTH;
            this.oldHeight = Constant.GAMEHIGHT;
            uiResize();
            if (dialogManager != null) {
                dialogManager.resize(width, height);
            }
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        if (dispose){
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    hideRootView();
                }
            });
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (viewpath!=null) {
            CocosResource.unLoadFile(viewpath);
        }
    }

    public void addActor(Actor addActor){
        stage.addActor(addActor);
    }


    public void setScreen(BaseScreen screen) {
        setScreen(screen,false);
    }
    public void setScreen(BaseScreen screen,boolean isGc) {
        if (isGc){
            game.setScreen(screen,isGc);
        }else {
            game.setScreen(screen);
        }
    }

    public void setScreen(Class<? extends BaseScreen> t) {
        setScreen(t,false);
    }

    public void setScreen(Class<? extends BaseScreen> t,boolean isGc) {
        Constructor<?> constructor = t.getConstructors()[0];
        try {
            BaseScreen baseScreen = (BaseScreen) constructor.newInstance(game);
            game.setScreen(baseScreen,isGc);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public <T extends Actor> T findActor(String name){
        return rootView.findActor(name);
    }

    public void showDialog(BaseDialog baseDialog){
        dialogManager.showDialog(baseDialog);
    }

    public void showDialog(Class<? extends BaseDialog> t){
        Constructor<?> constructor = t.getConstructors()[0];
        try {
            BaseDialog dialog = (BaseDialog) constructor.newInstance();
            dialogManager.showDialog(dialog);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void actorOffset(Actor actor,int align){
        if (align == Align.left) {
            actor.setX(actor.getX(Align.center)-offsetX,Align.center);
        }else if (align==Align.right){
            actor.setX(actor.getX(Align.center)+offsetX,Align.center);
        }else if (align == Align.bottom){
            actor.setY(actor.getY(Align.center)-offsetY,Align.center);
        }else if (align == Align.top){
            actor.setY(actor.getY(Align.center)+offsetY,Align.center);
        }
    }

    public void actorOffset(Actor actor,float baseXY,int align){
        if (align == Align.left) {
            actor.setX(baseXY-offsetX,Align.center);
        }else if (align==Align.right){
            actor.setX(baseXY+offsetX,Align.center);
        }else if (align == Align.bottom){
            actor.setY(baseXY-offsetY,Align.center);
        }else if (align == Align.top){
            actor.setY(baseXY+offsetY,Align.center);
        }
    }


    private void hideRootView() {
        ScreenResource annotation = AnnotationInfo.checkClassAnnotation(this, ScreenResource.class);
        if (annotation!=null){
            viewpath = annotation.value();
            CocosResource.unLoadFile(viewpath);
        }
    }


    public InputMultiplexer getMultiplexer() {
        return multiplexer;
    }

    public float getOffsetTop() {
        return offsetTop;
    }

    public float getOffsetLeft() {
        return offsetLeft;
    }

    public float getOffsetRight() {
        return offsetRight;
    }

    public float getOffsetBottom() {
        return offsetBottom;
    }

    public void zhuanCEnd() {
        game.zhuancEnd();
    }
}

