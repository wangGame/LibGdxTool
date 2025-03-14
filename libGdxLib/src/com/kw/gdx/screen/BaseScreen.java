package com.kw.gdx.screen;

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
import com.kw.gdx.view.dialog.DialogManager;
import com.kw.gdx.view.dialog.base.BaseDialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BaseScreen implements Screen {
    protected boolean dispose;
    protected final Stage stage;
    protected Group rootView;
    protected String viewpath;
    protected float offsetY;
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
    private Group uiGroup;
    private Group dialogGroup;
    private Group otherGroup;

    public BaseScreen(BaseGame game){
        activeScreen = true;
        this.screenSize = new Vector2(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        this.game = game;
        this.stage = new Stage(getStageViewport(), getBatch());
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
        Constant.camera = stage.getCamera();
        this.bannerManager = new BannerManager(stage);
        this.bannerManager.init(offsetY);
        if (Constant.DEBUG) {
            this.bannerManager.setVisible(true);
        }
        stage.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.R) {
                    r();
                }
                return super.keyUp(event, keycode);
            }
        });


        dialogGroup = new Group();
        this.dialogManager = new DialogManager(dialogGroup);
        this.centerX = Constant.GAMEWIDTH / 2;
        this.centerY = Constant.GAMEHIGHT / 2;
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        bannerHight = BannerView.pxToDp(Configuration.bannerHeight);
//        stage.getRoot().setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2,Align.center);
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
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        initTouch();
        initRootView();
        initAnnotation();
        initView();
    }

    public void initView(){}

    private void initRootView() {
        uiGroup = new Group();
        ScreenResource annotation = AnnotationInfo.checkClassAnnotation(this, ScreenResource.class);
        if (annotation!=null){
            viewpath = annotation.value();
            rootView = CocosResource.loadFile(viewpath);
            uiGroup.addActor(rootView);
            rootView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        }else {
            rootView = new Group();
            uiGroup.addActor(rootView);
            rootView.setSize(Constant.WIDTH,Constant.HIGHT);
            rootView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        }

        uiGroup.setName("uiGroup");
        stage.addActor(uiGroup);
        dialogGroup.setName("dialogGroup");
        otherGroup = new Group();
        otherGroup.setName("otherGroup");
        stage.addActor(otherGroup);
        stage.addActor(dialogGroup);

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

    @Override
    public void resize(int width, int height) {

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
        game.setScreen(screen);
    }

    public void setScreen(Class<? extends BaseScreen> t) {
        Constructor<?> constructor = t.getConstructors()[0];
        try {
            BaseScreen baseScreen = (BaseScreen) constructor.newInstance(game);
            game.setScreen(baseScreen);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T extends Actor> T findActor(String name){
        return rootView.findActor(name);
    }

    public void showDialog(BaseDialog baseDialog){
        dialogManager.showDialog(baseDialog);
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
}

