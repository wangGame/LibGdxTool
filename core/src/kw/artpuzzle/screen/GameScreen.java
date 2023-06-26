package kw.artpuzzle.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.BaseGame;

import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.sound.AudioProcess;
import com.kw.gdx.sound.AudioType;
import com.kw.gdx.utils.ads.BannerView;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.view.dialog.base.BaseDialog;
import com.kw.gdx.zip.PackZip;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.auto.Auto;
import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.csv.InitCsvData;
import kw.artpuzzle.csv.LevelOrder;
import kw.artpuzzle.dialog.AdsTry;
import kw.artpuzzle.downLoad.DownLoadLevelUtils;
import kw.artpuzzle.flurry.FlurryUtils;
import kw.artpuzzle.group.ShadowImageUtils;
import kw.artpuzzle.history.LevelHistoryUtils;
import kw.artpuzzle.level.LevelView;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.artpuzzle.signlistener.SignListener;
import kw.artpuzzle.theme.GameTheme;

@ScreenResource("cocos/gameView.json")
public class GameScreen extends UserBaseScreen {
    private LevelView gameView;


    public GameScreen(BaseGame game) {
        super(game);
        if (game instanceof ArtPuzzle) {
            ((ArtPuzzle)game).checkLevel();
        }
    }

    @Override
    public void initView() {
        super.initView();
        touchDisable();
        stage.addAction(Actions.delay(4f,Actions.run(()->{
            touchEnable();
        })));
        bannerManager.showBanner(false);
        Actor game_bg = rootView.findActor("game_bg");
        game_bg.setColor(GameTheme.bgColor);
        game_bg.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        game_bg.setPosition(360,640,Align.center);
        Group game_bottom = rootView.findActor("game_bottom");
        game_bottom.setY(24 + BannerView.pxToDp(50));
        actorOffset(game_bottom,Align.bottom);

        Actor bottom_num = game_bottom.findActor("bottom_num");
        bottom_num.setX(-offsetX);
        Actor bottom_bg = game_bottom.findActor("bottom_bg");
        bottom_bg.setWidth(Constant.GAMEWIDTH);
        bottom_bg.setX(game_bottom.getWidth()/2,Align.center);
        bottom_bg.setColor(Color.valueOf("#ebdab2"));
        Group game_top = rootView.findActor("game_top");
        actorOffset(game_top,Align.top);
        Actor ad = game_top.findActor("ad");
        actorOffset(ad,Align.right);
        loadingLevel();
        Actor top_back = game_top.findActor("top_back");

        actorOffset(top_back,Align.left);
        game_top.findActor("top_progress").setVisible(false);
        backBtn();


        bottom_bg.getColor().a = 0.0f;


        bottom_num.getColor().a = 0;
    }

    private void enterAnimation(){
        Group game_bottom = findActor("game_bottom");

        Actor bottom_bg = game_bottom.findActor("bottom_bg");
        bottom_bg.getColor().a = 0.0f;
        bottom_bg.addAction(Actions.fadeIn(1.0f));
        Group bottom_num = game_bottom.findActor("bottom_num");
        bottom_num.getColor().a = 0;
        bottom_num.addAction(Actions.fadeIn(1.0f));
//                float width = bottom_num.getWidth();
//                float x2 = bottom_num.getX()+8;
//                bottom_num.addAction(Actions.parallel(
//                        Actions.sequence( Actions.moveTo(x2 - width,bottom_num.getY()),
//                                Actions.delay(0.4667f),
//                                Actions.moveTo(x2,bottom_num.getY(),0.26667f)),
//                        Actions.sequence(Actions.alpha(0),
//                                Actions.delay(0.6333f),
//                                Actions.fadeIn(0.26667f,new BseInterpolation(0,0.01f,0.75f,1.0f)))
//                ));
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Group game_top = rootView.findActor("game_top");
                game_top.getColor().a = 0;
                game_top.addAction(Actions.fadeIn(0.6f));
//                Actor top_back = game_top.findActor("top_back");
//                float x = top_back.getX(Align.center);
//                float y = top_back.getY(Align.center);
//                top_back.getColor().a = 0;
//                top_back.addAction(
//                        Actions.parallel(
//                        Actions.sequence(
//                                Actions.moveToAligned(x+75.07f,y,Align.center),
//                                Actions.moveToAligned(x,y,Align.center,0.3f,
//                                        new BseInterpolation(0,0,0.75f,1))),
//                        Actions.sequence(
//                                Actions.alpha(0),
//                                Actions.fadeIn(0.2333f,new BseInterpolation(0,0,0.75f,1.0f))
//                                )
//                        ));
//
//
//                Actor top_tp_g = game_top.findActor("top_tp_g");
//                top_tp_g.getColor().a = 0;
//                top_tp_g.addAction(Actions.parallel(
//                        Actions.sequence(
//                                Actions.alpha(0),
//                                Actions.delay(0.6f),
//                                Actions.alpha(1.0f,0.2333f)
//                        ),
//                        Actions.sequence(
//                          Actions.scaleTo(0.622f,0.622f),
//                          Actions.delay(0.6f),
//                          Actions.scaleTo(1.067f,1.067f,0.233f,new BseInterpolation(0,0,0.75f,1)),
//                                Actions.scaleTo(1.0f,1.0f,0.1333f,new BseInterpolation(0.25f,0,1,1))
//                        )
//                ));
//
//
//                Actor top_ad_g = game_top.findActor("top_ad_g");
//                top_ad_g.getColor().a = 0;
//                top_ad_g.addAction(Actions.parallel(
//                        Actions.sequence(Actions.alpha(0),
//                                Actions.delay(0.6333f),
//                                Actions.alpha(1,0.26667f)),
//                        Actions.sequence(
//                                Actions.scaleTo(0.622f,0.622f),
//                                Actions.delay(0.6333f),
//                                Actions.scaleTo(1.067f,1.067f,0.26667f,new BseInterpolation(0,0,0.75f,1)),
//                                Actions.scaleTo(1.0f,1.0f,0.1333f,new BseInterpolation(0.25f,0,1,1))
//                        )
//                ));




//                float x1 = bottom_bg.getX();
//                float y1 = bottom_bg.getY();
//                bottom_bg.addAction(Actions.parallel(
//                        Actions.sequence(
//                            Actions.alpha(0),
//                            Actions.fadeIn(0.447f)
//                        ),
//                        Actions.sequence(
//                                Actions.moveTo(x1+724.27f,y1),
//                                Actions.moveTo(x1,y1,0.4667f,new BseInterpolation(0,0,0.75f,1.0f))
//                        )));
//
            }
        });


        

    }

//    public Action enterAl(){
//        return Actions.parallel(
//                Actions.sequence(Actions.alpha(0,0),
//                        Actions.fadeIn(0.2333f,new BseInterpolation(
//                                0,0,0.75f,1.0f
//                        )))
////                Actions.sequence(Actions.scaleTo(0,0),
////                        Actions.delay(0.5f),
////                        Actions.scaleTo(1,1,0.2f,
////                                new BseInterpolation(0,0,0.75f,1.0f)))
//        );
//    }

    public void guide(){
        if (!LevelConfig.guide)return;
        if (LevelConfig.gameStatus == LevelConfig.ENTERGAME){
            gameView.addAction(Actions.delay(5f,Actions.run(()->{
                gameView.tip();
            })));
        }else {

        }
    }

    private void backBtn() {
        Actor top_back = findActor("top_back");
        top_back.setTouchable(Touchable.enabled);
        top_back.setOrigin(Align.center);
        top_back.addListener(new OrdinaryButtonListener(0.87f){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                LevelHistoryUtils.saveHistory(gameView.getHistoryData());
                LevelHistoryUtils.historyPercent(gameView.getPercent());
                top_back.clearActions();
                if (LevelConfig.countTime >= LevelConfig.interstitial) {
                    GameStaticInstance.gameListener.showInterstitial(20000L);
                }
                if (gameView!=null) {
                    FlurryUtils.guideStepNum(gameView.getCurrentCount());
                }
                enterMainScreen();
            }
        });
    }

    private void enterMainScreen(){
        AudioProcess.playSound(AudioType.clickA);
        loadingAnimation();
        stage.addAction(Actions.delay(0.8f,Actions.run(()->{
            gameView.dispose();
            setScreen(MainScreen.class);

        })));
    }

    private void loadingLevel() {
        hideView();
        loadingAnimation();
        NLog.i("loding level info !");
        FileHandle tarGetFileDir;
        FileHandle mdFiveFile;
        String version = InitCsvData.orderHashMap.get(LevelConfig.levelNum).getVersion();
        if (LevelConfig.useInocal){
            tarGetFileDir = Gdx.files.internal(version + "/" +LevelConfig.levelDirPath + LevelConfig.levelNumIndex);
            mdFiveFile = Gdx.files.internal(version + "/" +LevelConfig.levelDirPath + LevelConfig.levelNumIndex+LevelConfig.mdFileName);
        }else {
            tarGetFileDir = Gdx.files.local(version + "/" +LevelConfig.levelDirPath + LevelConfig.levelNumIndex);
            mdFiveFile = Gdx.files.local(version + "/" +LevelConfig.levelDirPath + LevelConfig.levelNumIndex+LevelConfig.mdFileName);
       }
        //特殊处理
        if (LevelConfig.levelNum == 1){
            tarGetFileDir = Gdx.files.internal(version + "/" +LevelConfig.levelDirPath + LevelConfig.levelNumIndex);
            mdFiveFile = Gdx.files.internal(version + "/" +LevelConfig.levelDirPath + LevelConfig.levelNumIndex+LevelConfig.mdFileName);
        }
        if (!mdFiveFile.exists()) {
            if (LevelConfig.useInocal){
                BaseGame.setText("file not exist!");
                return;
            }
        }
        if (mdFiveFile.exists() && (PackZip.check(tarGetFileDir)||LevelConfig.useInocal)) {
//        if (mdFiveFile.exists() || (LevelConfig.useInocal)) {
            BaseGame.setText("reource pre finish !!!");
            stage.addAction(Actions.delay(1,Actions.run(()->{
                showView();
            })));
        }else {
            download();
        }
    }

    @Override
    protected void removeLoading() {
        super.removeLoading();
        if (resumeLoading && loadingImage!=null){
            loadingImage.remove();
        }
    }

    @Override
    public boolean loadingAnimation(){
        GameStaticInstance.gameListener.hideBanner();
        if (loadingImage!=null){
            return false;
        }
        if (LevelConfig.enterGame) {
            LevelConfig.enterGame = false;
            if (gameView != null) {
                if (gameView.getHandImage() != null) {
                    gameView.getHandImage().clearActions();
                    gameView.getHandImage().remove();
                }
                gameView.setLoading(true);
            }
        }

        loadingImage = CocosResource.loadFile("cocos/gameloading.json");
        addActor(loadingImage);
        loadingImage.addAction(new Action() {
            @Override
            public boolean act(float v) {
                if (loadingImage!=null) {
                    loadingImage.toFront();
                }
                return false;
            }
        });
        loadingImage.setName("loadingpage");
        loadingImage.setOrigin(Align.center);
        Actor gloading_bg = loadingImage.findActor("gloading_bg");
        gloading_bg.setTouchable(Touchable.enabled);
        gloading_bg.setColor(GameTheme.bgColor);
        gloading_bg.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        gloading_bg.setPosition(loadingImage.getWidth()/2,loadingImage.getHeight()/2,Align.center);
        loadingImage.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        Actor gloading_line = loadingImage.findActor("gloading_line");
        {
            SpineActor loadingAnimation = new SpineActor("spine/2_0_loading");
            loadingImage.addActor(loadingAnimation);
            loadingAnimation.setName("loadingnimation");
            loadingAnimation.setAnimation("loading", true);
            loadingAnimation.setPosition(gloading_line.getX(Align.center) - 15, 712, Align.center);
            loadingAnimation.getColor().a = 0;
            loadingAnimation.addAction(Actions.fadeIn(0.2f));
        }

        gloading_line.remove();
        return true;
    }


    private void download() {
        NLog.e("Level %s is not found or md5check error!",LevelConfig.levelNumIndex);
        String version = InitCsvData.orderHashMap.get(LevelConfig.levelNum).getVersion();
        DownLoadLevelUtils downLoad = new DownLoadLevelUtils(
                game,
                LevelConfig.levelNumIndex,version,
                new Runnable() {
            @Override
            public void run() {
                NLog.i("download finish!");
                showView();
            }
        }, new Runnable() {
            @Override
            public void run() {
                stage.addAction(Actions.delay(10,Actions.run(()->{
                    download();
                })));
            }
        });
        downLoad.downLoad();


    }
    SpineActor tipAnimation;
    private void showView() {
        stage.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                NLog.e("stage click debug touchUp ---------- ");
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                NLog.e("stage click debug  touchDown---------- ");
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                NLog.e("stage click debug  dragged---------- ");
                super.touchDragged(event, x, y, pointer);
            }
        });
        NLog.i("enter level:%s ",LevelConfig.levelNum);
        //展示的时候出现广告

        //加载资源
        ///
        if (gameView!=null){
            gameView.setLoading(false);
        }
        loadingImage.addAction(
                Actions.sequence(
                    Actions.fadeOut(0.1f),
                        Actions.run(()->{
                            loadingImage.remove();
                            loadingImage = null;
                        })));
        Group middle = findActor("middle");
        if (ArtPuzzlePreferece.getInstance().getCurrentLevel()>=4){
            bannerManager.showBanner(true);
        }
        gameView = new LevelView(rootView,new Runnable(){
            @Override
            public void run() {
                loadingAnimation();
                if (loadingImage!=null) {
                    Actor actor = loadingImage.findActor("gloading_bg");
                    actor.getColor().a = 0;
                    SpineActor actor1 = loadingImage.findActor("loadingnimation");

                    actor.addAction(
                            Actions.parallel(
                                Actions.fadeIn(0.3f),
                                Actions.delay(0.4f,Actions.run(()->{
                                    SpineActor actor2 = loadingImage.findActor("loadingnimation");
                                    actor2.setVisible(true);

                                })
                                    )));
                }
            }
        });
        middle.addActor(gameView);
        middle.setY(middle.getY() - offsetY);
        if (LevelConfig.gameStatus == LevelConfig.ENTERGAME){
            gameView.initView();
            showUIView();
            GameStaticInstance.gameListener.showBanner();
        }else {
            LevelOrder levelOrder = InitCsvData.orderHashMap.get(LevelConfig.levelNum);
            FlurryUtils.uiView(levelOrder.getLevel_id());
            Actor img_border = rootView.findActor("img_border");
            img_border.setVisible(false);
            gameView.successDisplay(false);
            gameView.setSpineAnimationVisible();
        }
        gameView.setX(middle.getWidth()/2,Align.center);


//        Actor levelPicGroup = gameView.getLevelPicGroup();
//        Actor img_border = rootView.findActor("img_border");
//        img_border.setSize(levelPicGroup.getWidth() * levelPicGroup.getScaleX(),
//                (levelPicGroup.getHeight()+1)* levelPicGroup.getScaleX());
//        img_border.setColor(Color.valueOf("#f5ecd0"));
//        Vector2 tempV2 = new Vector2(levelPicGroup.getX(Align.center),
//                levelPicGroup.getY(Align.center));
//        levelPicGroup.getParent().localToParentCoordinates(tempV2);
//        img_border.setPosition(tempV2.x,tempV2.y,Align.center);
//        img_border.toFront();

//        Actor levelPicGroup = gameView.getLevelPicGroup();
//        Actor img_border = rootView.findActor("img_border");
//        img_border.setSize(
//                levelPicGroup.getWidth() * levelPicGroup.getScaleX(),
//                (levelPicGroup.getHeight()+1)* levelPicGroup.getScaleX());
//        img_border.setColor(Color.valueOf("#f5ecd0"));
//        Vector2 tempV2 = new Vector2(
//                levelPicGroup.getX(Align.center),
//                levelPicGroup.getY(Align.center));
//        levelPicGroup.getParent().localToParentCoordinates(tempV2);
//        img_border.setPosition(tempV2.x,tempV2.y,Align.center);
//        img_border.toFront();


        tipAnimation = new SpineActor("spine/hint");
        Actor top_back = findActor("top_back");
        top_back.toFront();
        GameStaticInstance.gameListener.auto(gameView);
        Group ad = rootView.findActor("ad");
        ad.addActor(tipAnimation);
        Actor top_ad_g = ad.findActor("top_ad_g");
        Actor top_tp_g = ad.findActor("top_tp_g");
        tipAnimation.setPosition(top_tp_g.getX(Align.center),top_tp_g.getY(Align.center),Align.center);
        tipAnimation.toBack();
        top_tp_g.setVisible(false);
        Image image = new Image(Asset.getAsset().getTexture("cocos/gameasset/1.png"));
        ad.addActor(image);
        image.setName("gamehintNum");
        image.setVisible(false);
        image.setPosition(top_ad_g.getX(Align.center),top_ad_g.getY(Align.center),Align.center);
        ad.addListener(new OrdinaryButtonListener(0.87f){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//
////                if (GameStaticInstance.gameListener.isNetConnect()){
////                    if (GameStaticInstance.gameListener.)
////                }
//

                if (!gameView.isAble()) return;
                if (LevelConfig.guide){

//                    gameView.tip();
                }else {
                    if (!ArtPuzzlePreferece.getInstance().isHintStatus()) {
                        if (GameStaticInstance.gameListener.isVideoAlready()) {
                            GameStaticInstance.gameListener.showAdsVideo(null);
                        }else {
                            dialogManager.showDialog(new AdsTry());
                        }
                    } else {
                        image.setVisible(false);
                        top_ad_g.setVisible(true);
                        ArtPuzzlePreferece.getInstance().updateHintStatus(false);
                        FlurryUtils.hint(LevelConfig.levelNum);
                        gameView.tip();
                    }
                }
            }
        });
        ad.addAction(new Action() {
            @Override
            public boolean act(float v) {
                if (ArtPuzzlePreferece.getInstance().isHintStatus()){
                    image.setVisible(true);
                    top_ad_g.setVisible(false);
                }
                return false;
            }
        });
        if (false) {
            Group group = CocosResource.loadFile("cocos/CommonButton.json");
            group.getColor().a = 0;
            group.setScale(1.f);
            group.setTouchable(Touchable.enabled);
            Group game_top1 = findActor("game_top");
            game_top1.addActor(group);
            group.setX(game_top1.getWidth() / 2, Align.center);
            group.setY(20);
            Label label = group.findActor("buttonLabel");
            label.setText("show ceng num " + gameView.getLevelCengNum());
            group.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    gameView.nextCeng(new SignListener() {
                        @Override
                        public void sign(Object o) {
                            label.setText("show ceng num " + (int) o);
                        }
                    });
                }
            });
        }
        enterAnimation();

        if (LevelConfig.levelNum == 1){
            guide();
        }
    }

    private void showUIView() {
        rootView.findActor("game_top").setVisible(true);
        rootView.findActor("game_bottom").setVisible(true);
    }

    public void hideView(){
        rootView.findActor("game_top").setVisible(false);
        rootView.findActor("game_bottom").setVisible(false);
    }

    private boolean backed = false;
    @Override
    protected BaseDialog back() {
        BaseDialog back = super.back();
        if (back==null) {
            if (backed)return back;
            if (gameView !=null) {
                LevelHistoryUtils.saveHistory(gameView.getHistoryData());
                LevelHistoryUtils.historyPercent(gameView.getPercent());
            }
            loadingAnimation();
            backed = true;
            if (LevelConfig.countTime >= LevelConfig.interstitial) {
                if (LevelConfig.ENTERGAME == LevelConfig.PLAY) {
                    GameStaticInstance.gameListener.showInterstitial();
                }
            }
            stage.addAction(Actions.delay(0.3f,Actions.run(()->{
                if (gameView !=null) {
                    FlurryUtils.guideStepNum(gameView.getCurrentCount());
                    gameView.dispose();
                }
                Constant.currentScreen.setScreen(MainScreen.class);
            })));
        }
        return back;
    }



    @Override
    public void pause() {
        super.pause();
        if (gameView !=null) {
            LevelHistoryUtils.saveHistory(gameView.getHistoryData());
            LevelHistoryUtils.historyPercent(gameView.getPercent());
        }
    }



    @Override
    public void render(float delta) {
        super.render(delta);
        float oldtime = LevelConfig.hintTimeCount;
        LevelConfig.hintTimeCount += delta;
        if (oldtime<=LevelConfig.hintTime){
            if (LevelConfig.hintTimeCount>=LevelConfig.hintTime){
                if (tipAnimation!=null) {
                    tipAnimation.setAnimation("animation", false);
                }
                LevelConfig.hintTime = 10;
                LevelConfig.hintTimeCount = 0;
            }
        }
    }

    @Override
    public void resume() {
        super.resume();

    }

    @Override
    public void hide() {
        super.hide();
    }

}
