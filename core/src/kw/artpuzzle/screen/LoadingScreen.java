package kw.artpuzzle.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.BaseGame;

import com.kw.gdx.animation.effect.EffectTool;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.sound.AudioProcess;
import com.kw.gdx.sound.AudioType;

import kw.artpuzzle.PathAnimation;
import kw.artpuzzle.asset.FontResource;
import kw.artpuzzle.asset.SpineRes;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.constant.NetContant;
import kw.artpuzzle.csv.InitCsvData;
import kw.artpuzzle.dialog.ModelDialog;
import kw.artpuzzle.flurry.FlurryUtils;
import kw.artpuzzle.group.ProcessUtils;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.artpuzzle.test.TestGroup;
import kw.artpuzzle.theme.GameTheme;

@ScreenResource("cocos/loadingscreen.json")
public class LoadingScreen extends BaseScreen {
    private float countTime = 0;
    private Image process;
    private float processWidth;
    private Label progressLabel;
    private Array<Vector2> pos;
    private Image temp;

    public LoadingScreen(BaseGame game) {
        super(game);
        dispose = true;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop||
                 Gdx.app.getVersion()<20) {
            NetContant.url = LevelConfig.desktopOrLowVersionUrl;
        }else {
            NetContant.url = LevelConfig.url;
        }
        LevelConfig.countTime = 20;
    }

    @Override
    public void initView() {
        super.initView();




        double arr[] = {
                35.8, -58.85, 19.66, -60.46, 2.82, -62.13, -38.42, -59.86, -50.4, -40.21, -61.95,
                -21.26, -67.48, 24.16, -29.49, 44.39, -6.21, 56.78, 48.21, 44.12, 44.59, -0.95,
                42.56, -26.22, 12.09, -48.33, -14.97, -35.68, -33.4, -27.07, -36.2, -10.49, -33.74,
                5.5, -31.41, 20.68, -17.14, 31.15, -3.31, 33.2, 3.85, 34.27, 20.67, 29.67, 26.44, 18.46,
                32.8, 6.14, 29.86, -16.29, 10.78, -24.21, -2.94, -29.91, -18.99, -22.57, -23.67,
                -10.3, -27.73, 0.33, -23.82, 20.65, -4.18, 24.92, 2.48, 26.36, 19.41, 23.09, 22.12,
                10.01, 23.38, 3.92, 23.44, -15.35, 0.55, -16.06, -3.8, -16.19, -16.97, -11.04, -15.79, 0.46, -14.28, 15.16,
                -2.55, 18.31, 4.25, 15.99, 8.86, 14.42, 14.99, 0.58, 1.49, -0.6, -14.26, -1.97
        };
        int index = 0;
        pos = new Array<>();
        for (int i = 0; i < arr.length/2; i++) {
            Vector2 vector2 = new Vector2();
            vector2.set((float) arr[index++],(float) arr[index++]);
            pos.add(vector2);
        }

        temp = new Image(Asset.getAsset().getTexture("common/white_bg.png"));
        addActor(temp);
        temp.setColor(Color.BLACK);

        PathAnimation pathAnimation = new PathAnimation();
        pathAnimation.setPosData(arr,0,0.25f);
        pathAnimation.setDuration(2);
        PathAnimation pathAnimation1 = new PathAnimation();
        pathAnimation1.setPosData(arr,0.25f,1.0f);
        pathAnimation1.setDuration(3);
        temp.addAction(
                Actions.sequence(
                pathAnimation,
                pathAnimation1
        ));
        temp.setPosition(600,600);


        //        SpineActor spineActor = new SpineActor("heidong1");
//        addActor(spineActor);
//        spineActor.setPosition(300,300);
//        spineActor.setAnimation("animation",true);


//        LevelConfig.gamePlayTime = 0;
//        Texture texture = Asset.getAsset().getTexture("common/white_bg.png");
//        Image bg = new Image(texture);
//        bg.setColor(GameTheme.bgColor);
//        bg.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
//        addActor(bg);
//        bg.toBack();
//        bg.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2,Align.center);
//        process = rootView.findActor("progress");
//        processWidth = rootView.findActor("progressbg").getWidth();
//
//        Actor logo = rootView.findActor("logo");
//        logo.setVisible(false);
//
//        SpineActor loaingAnimation = new SpineActor("spine/0_loading");
//        stage.addAction(Actions.delay(0.1f,Actions.run(()->{
//            rootView.addActor(loaingAnimation);
//        })));
////        loaingAnimation.setPosition(Constant.GAMEWIDTH/2,690.0f/1280.0f * Constant.GAMEHIGHT);
//        loaingAnimation.setPosition(logo.getX(Align.center),
//                (logo.getY(Align.center))/1280.0f*Constant.GAMEHIGHT,Align.center);
//        loaingAnimation.setAnimation("animation4",false);
//
//        LevelConfig.guide = ArtPuzzlePreferece.getInstance().isGuide();
//        FontResource.getInstance().loadRes();
//        SpineRes.getInstance().loadRes();
//        AudioProcess.prepare(AudioType.class);
//        new InitCsvData();
//        LevelConfig.enterNum = 0;
//        progressLabel = rootView.findActor("BitmapFontLabel_1");
    }

    private int ii = 0;
    @Override
    public void render(float delta) {
        countTime+=delta;

//        progressLabel.setText((int) (Asset.getAsset().getProcess() * 100)+"%");
//        process.setWidth(Asset.getAsset().getProcess() * processWidth);
        if (Asset.getAsset().update() && countTime>3&&activeScreen) {
//            Constant.isHaptic = ArtPuzzlePreferece.getInstance().isHaptic();
//
//            ArtPuzzlePreferece.getInstance().isFinsh();
//            AudioProcess.loadFinished();
//            LevelConfig.levelNum = ArtPuzzlePreferece.getInstance().getCurrentLevel();
//            activeScreen = false;
//            FontResource.getInstance().getRes();
//            SpineRes.getInstance().loadRes();
//            ArtPuzzlePreferece.getInstance().isSound();
//            ArtPuzzlePreferece.getInstance().isHaptic();
//            if (Constant.DEBUG) {
//                showDialog(new ModelDialog(this));
//            }else {
//                LevelConfig.ISSHUFFLE = LevelConfig.SHUFFLE;
//                LevelConfig.useInocal = false;
//                if (LevelConfig.guide){
////                    FlurryUtils.guideLoading();
//                    LevelConfig.levelNum = 1;
//                    LevelConfig.levelNumIndex = InitCsvData.orderHashMap.get(1).getLevel_num();
//                    game.setScreen(new GameScreen(game));
//                    LevelConfig.mainNeedLoadingAnmation = LevelConfig.GameEnterMain;
//                    if (LevelConfig.isFristEnterGame) {
//                        FlurryUtils.fristStartLoading();
//                    }
//                }else {
//                    LevelConfig.mainNeedLoadingAnmation = LevelConfig.LoadingEnterMain;
//                    game.setScreen(new MainScreen(game));
//                }
//            }
        }
        super.render(delta);
    }
}
