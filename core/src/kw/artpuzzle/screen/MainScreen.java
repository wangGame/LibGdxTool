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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.BaseGame;


import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.label.Label4;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.utils.log.StringUtils;
import com.kw.gdx.view.dialog.base.BaseDialog;
import com.kw.gdx.zip.PackZip;

import java.util.logging.Level;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.csv.InitCsvData;
import kw.artpuzzle.csv.LevelOrder;
import kw.artpuzzle.dialog.NoInterNetDialog;
import kw.artpuzzle.dialog.SettingDialog;
import kw.artpuzzle.downLoad.DownLoadPreUtils;
import kw.artpuzzle.flurry.FlurryUtils;
import kw.artpuzzle.group.BottomPanel;
import kw.artpuzzle.group.LevelItemGroup;
import kw.artpuzzle.group.ShadowImageUtils;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.artpuzzle.scroll.ScrollPane;
import kw.artpuzzle.signlistener.SignListener;
import kw.artpuzzle.theme.GameTheme;

@ScreenResource("cocos/mainView.json")
public class MainScreen extends UserBaseScreen {
    private boolean isBusy;
    private ArrayMap<Integer,LevelItemGroup> prePicTask;
    private ArrayMap<Integer,LevelItemGroup> prePicAllTask;
    private ArrayMap<Integer,Integer> prePicAllTaskIndex;
    private int levelScrollPanelIndex = 1;
    private int maxLevelScrollPanelIndex = 150;
    private float topHight = 104;
    private int rowNum;
    private ScrollPane middleScrollPanel;

    public MainScreen(BaseGame game) {
        super(game);
        this.prePicTask = new ArrayMap<>();
        this.prePicAllTask = new ArrayMap<>();
        this.prePicAllTaskIndex = new ArrayMap<>();
        GameStaticInstance.gameListener.hideBanner();
//        stage.setDebugAll(true);


    }

    @Override
    protected void removeLoading() {
        if(loadingImage!=null) {
            loadingImage.remove();
        }
    }

    @Override
    public void initView() {
        super.initView();
        touchDisable();
        stage.addAction(Actions.delay(0.6f,Actions.run(()->{
            touchEnable();
        })));
        if (game instanceof ArtPuzzle) {
            ((ArtPuzzle)game).checkLevel();
        }
        bannerManager.setVisible(false);
        initBg();
        setThemeColor();
        initTopView();
        bottomView();
        Gdx.graphics.requestRendering();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                updateTask();
            }
        });
        test();

        if (LevelConfig.mainNeedLoadingAnmation != LevelConfig.LoadingEnterMain) {

            loadingAnimation();
            if (loadingImage!=null) {
                Actor gloading_bg = loadingImage.findActor("gloading_bg");
                gloading_bg.addAction(
                            Actions.fadeOut(0.4f)
                );
                Actor loading = loadingImage.findActor("loading");
//                loading.addAction(Actions.delay(0.5f,Actions.visible(false)));
                loading.setVisible(false);
            }
        }else {
            LevelConfig.mainNeedLoadingAnmation = LevelConfig.GameEnterMain;
            int index = -1;


            Actor main_top = findActor("main_top");
            main_top.getColor().a = 0;
            main_top.addAction(Actions.fadeIn(0.6f,
                    new BseInterpolation(0.25f,0,1,1)));

            for (Actor actor1 : animationArrayActor) {
                Actor actor = ((Group)actor1).findActor("panel");
                float y = actor.getY();
                actor.setY(y-522.15f);
                index ++;
                actor1.getColor().a = 0.1f;
                actor1.addAction(Actions.sequence(
                        Actions.delay(index/2 * 0.1f),
                        Actions.fadeIn(0.333f,
                                new BseInterpolation(0,0.01f,0.484f,1.0f))
                ));
                actor.addAction(
                        Actions.sequence(
                                Actions.delay(index / 2 * 0.1f),
                                Actions.moveTo(
                                        actor.getX(),
                                        y+6.34f,
                                        0.4667f,
                                        new BseInterpolation(0f,0.01f,0.484f,1.0f)),
                                Actions.moveTo(
                                        actor.getX(),
                                        y,
                                        0.2333f,
                                        new BseInterpolation(0,0f,0.342f,1.0f))
                        ));
            }
        }

    }


    @Override
    public boolean loadingAnimation(){
        GameStaticInstance.gameListener.hideBanner();
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
        gloading_bg.setColor(GameTheme.bgColor);
        gloading_bg.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        gloading_bg.setPosition(loadingImage.getWidth()/2,loadingImage.getHeight()/2,Align.center);
        loadingImage.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        Actor gloading_line = loadingImage.findActor("gloading_line");
        {
            SpineActor loadingAnimation = new SpineActor("spine/2_0_loading");
            loadingImage.addActor(loadingAnimation);
            loadingAnimation.setAnimation("loading", true);
            loadingAnimation.setName("loading");
            loadingAnimation.setPosition(gloading_line.getX(Align.center) - 15, 712, Align.center);
        }

        gloading_line.remove();
        return true;
    }


    Array<Integer> animationArray = new Array<>();
    Array<Actor> animationArrayActor = new Array<>();
    private void calAnimationActor() {
//        {
//            int levelNum = LevelConfig.levelNum;
//            for (int i = levelNum - 5; i < levelNum + 5; i++) {
//                animationArray.add(i);
//            }
//        }
    }



    private void updateTask() {
        {
            int nnn = (int) ((middleScrollPanel.getScrollY() + 170) / 350 * 2) - 1;
            float v = Constant.GAMEHIGHT;
            float v1 = (float) Math.ceil(v / 324.0f);
            int v2 = (int) (v1 * 2);
            prePicTask.clear();
            for (int i = 0; i < v2 + 2; i++) {
                if (prePicAllTaskIndex.containsKey(nnn + i)) {
                    if (prePicAllTask.get(prePicAllTaskIndex.get(nnn + i)) == null) {
                        prePicAllTask.removeKey(prePicAllTaskIndex.get(nnn + i));
                    }else {
                        prePicTask.put(prePicAllTaskIndex.get(nnn + i), prePicAllTask.get(prePicAllTaskIndex.get(nnn + i)));
                    }
                }
            }
        }
    }

    private float basePositionY = 0;
    private float baseLinePositionX = 0;
    private float baseLineWidth = 0;

    private void initBg() {
        Actor mainbg = rootView.findActor("mainbg");
        mainbg.setSize(screenSize.x,screenSize.y);
        mainbg.setPosition(360,640,Align.center);
        mainbg.setColor(GameTheme.bgColor);
    }

    private void setThemeColor() {
        Group main_top = rootView.findActor("main_top");
        extracted(main_top, "top_title");

        Actor top_setting = rootView.findActor("top_setting");
        top_setting.setX(top_setting.getX(Align.center) + 30,Align.center);

//        extracted(main_top, "top_setting");
//        extracted(main_top, "top_row_left");
//        extracted(main_top, "top_row_right");
    }

    private void extracted(Group main_top, String top_title) {
        main_top.findActor(top_title).setColor(GameTheme.mainColor);
    }

    private boolean addActorTemp;
    private boolean isUpdate = false;
    private float lastPosY = 0;
    private void initTopView() {
        Group main_top = findActor("main_top");
        actorOffset(main_top,Align.top);
        Actor top_setting = main_top.findActor("top_setting");
        actorOffset(top_setting,Align.right);
        top_setting.setTouchable(Touchable.enabled);
        top_setting.setOrigin(Align.center);
        top_setting.addListener(new OrdinaryButtonListener(0.87f){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                dialogManager.showDialog(new SettingDialog());
            }
        });
        Label top_title = main_top.findActor("top_title");
        top_title.setText("Gallery");
        top_title.setColor(Color.valueOf("#cc6256"));


        Label4 tiHuanTile = new Label4("Gallery",new Label.LabelStyle(){{
            font = top_title.getStyle().font;
        }});
        top_title.getParent().addActor(tiHuanTile);
        tiHuanTile.setAlignment(Align.center);
        tiHuanTile.setColor(Color.valueOf("#cc6256"));
        tiHuanTile.setName(top_title.getName());
        tiHuanTile.setPosition(top_title.getX(Align.center),top_title.getY(Align.center),Align.center);
        top_title.remove();
        tiHuanTile.setModkern(1.7f);


        //计算会参与动画的actor



        calAnimationActor();
        Table scrollPanelTable = new Table(){
            {
//                Actor actor = new Actor();
//                actor.setWidth(Constant.GAMEWIDTH);
//                actor.setHeight(30);
//                add(actor);
            }
        };
        int initNum = LevelConfig.levelNum/2 + 5;
        rowNum = (int) (Constant.GAMEWIDTH / 350);

        Actor line = findActor("line");
        line.setY(line.getY(Align.center)+offsetY);
        line.setColor(Color.valueOf("cc6256"));
        line.getColor().a = 0.6f;

        line.setWidth(rowNum * 324 + (rowNum - 1) * 24);
        baseLineWidth = line.getWidth();
        line.setX(baseLinePositionX,Align.center);
        middleScrollPanel = new ScrollPane(scrollPanelTable,new ScrollPane.ScrollPaneStyle()){
            @Override
            public void act(float delta) {
                super.act(delta);

                float scrollPercentY = getVisualAmountY();
                if (scrollPercentY < Integer.MAX_VALUE && scrollPercentY > Integer.MIN_VALUE) {
                    float v = basePositionY - scrollPercentY;

                    lastPosY = scrollPercentY;
//                    float clamp = MathUtils.clamp(line.getY() + v, 0, line.getHeight());
//                    floatPanel.setY(clamp);
//                    lastPosY = scrollPercentY;
//                    if (scrollPercentY < 1) {
//                        floatPanel.setY(0);
//                    }
                    float v1 = baseLineWidth - v * 0.5f;
                    if (v1<baseLineWidth){
                        v1 = baseLineWidth;
                    }
                    line.setWidth(v1);
                    line.setX(baseLinePositionX,Align.center);
                }




                if (!adding&&getScrollPercentY()>0.9F){
                    adding = true;
                    addPanel(scrollPanelTable,rowNum);
                }

                if (isFlickScrollTouchUp()&&getFlingTimer()<=0.01f) {
                    if (isUpdate)return;
                    isUpdate = true;
                    int nnn = (int) ((getScrollY() + 170) / 350 * 2) - 1;
                    float v = Constant.GAMEHIGHT;
                    float v1 = (float) Math.ceil(v / 324.0f);
                    int v2 = (int) (v1 * 2);
                    prePicTask.clear();
                    for (int i = 0; i < v2 + 2; i++) {
                        if (prePicAllTaskIndex.containsKey(nnn + i)) {
                            if (prePicAllTask.get(prePicAllTaskIndex.get(nnn + i)) == null) {
                                NLog.e("----------------------------------------------------");
                            }else {
                                prePicTask.put(prePicAllTaskIndex.get(nnn + i), prePicAllTask.get(prePicAllTaskIndex.get(nnn + i)));
                            }
                        }
                    }
                }else {
                    isUpdate = false;
                }
            }
        };
        addActor(middleScrollPanel);
        Vector2 vector2 = new Vector2();
        Vector2 set = vector2.set(line.getX(Align.center), line.getY(Align.center));
        line.getParent().localToStageCoordinates(set);
        addActor(line);
        line.setPosition(Constant.GAMEWIDTH/2,set.y,Align.center);
        baseLinePositionX = line.getX(Align.center);
        line.toFront();
        addActorTemp = true;
        for (int i = 0; i < initNum; i++) {
            addPanel(scrollPanelTable,rowNum);
        }
        addActorTemp = false;
        float distance = 50; //banner distance scroll bottom add top distance scroll top
//        middleScrollPanel.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT- topHight - distance - bannerHight);
        middleScrollPanel.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT-topHight+1);
        middleScrollPanel.validate();
        int playRowNum = (ArtPuzzlePreferece.getInstance().getCurrentLevel() + 2) / 2;
        if (LevelConfig.MAININITPOS==LevelConfig.MAININITPOS_CURRENT_LEVEL) {
            playRowNum = (LevelConfig.levelNum + 1)/ rowNum;
        }
        //                          num * every hight  + (num -1) * distance  - scrollPanel/3    - everyH/2
        basePositionY = playRowNum*324+24*(playRowNum-1) + 324 / 2.0f + 24 * 2 - 324 - middleScrollPanel.getHeight()/2.0f;


        if (LevelConfig.mainNeedLoadingAnmation == LevelConfig.LoadingEnterMain){
            basePositionY = (playRowNum-1)*324+24*(playRowNum-1) + 12 - (2-1)*324-24*(2-1);
        }

        middleScrollPanel.setScrollY(basePositionY);
        middleScrollPanel.updateVisualScroll();

        middleScrollPanel.setY(Constant.GAMEHIGHT - 115 + 12, Align.top);
        basePositionY = middleScrollPanel.getScrollY();
//        Image image = new Image(new Texture("level4.png"));
//        image.setSize(234,234);
//        BufferBufferUtils utils = new BufferBufferUtils(image);
//        utils.setDebug(true);
//        TextureRegion colorBufferTexture = utils.getBufferTexture(1.0f);
//        addActor(utils);
//        Image o = new Image(colorBufferTexture);
//        addActor(o);
//        o.setVisible(false);


//        scrollPanelTable.

    }

    private void addPanel(Table levelItemTable,int num) {
        if (levelScrollPanelIndex >maxLevelScrollPanelIndex){
            return;
        }
        for (int i = 0; i < num; i++) {
            levelItemTable.add(createItemGroup()).padLeft(12).padRight(12).padTop(24F);
        }
        levelItemTable.row();
        levelItemTable.pack();
        if (levelScrollPanelIndex == maxLevelScrollPanelIndex+1){
            int index = (int) Math.floor (num / 2.0f);
            for (int i = 0; i < num; i++) {
                Group g = new Group();
                g.setSize(324, 180);

                if (i == index) {
                    Label label = new Label("More puzzles are coming soon",new Label.LabelStyle(){{
                        font = Asset.getAsset().loadBitFont("cocos/font/inter-regular-30.fnt");
                    }});
                    g.addActor(label);
                    label.setColor(Color.valueOf("#b88967"));
                    label.setPosition(-12,g.getHeight()/2,Align.center);
                }
                levelItemTable.add(g).padLeft(12).padRight(12);
            }
            levelItemTable.row();
            levelItemTable.pack();
        }

        adding = false;
    }

    private LevelItemGroup createItemGroup() {
        LevelOrder levelOrder = InitCsvData.orderHashMap.get(levelScrollPanelIndex);
        int index = levelScrollPanelIndex;
        if (levelOrder!=null){
            index = levelOrder.getLevel_num();
        }else {
            NLog.e(levelScrollPanelIndex +":level order is null");
        }
        LevelItemGroup levelItemGroup = new LevelItemGroup(levelScrollPanelIndex,index, new LevelItemGroup.ItemListener() {
            @Override
            public void openLevel(int levelId,int levelScrollPanelIndex,int status) {
                //current play level
                if (status == LevelConfig.PLAYING){
                    //split
                    if (interNetDialog(levelScrollPanelIndex)) return;
                    LevelConfig.levelNum = levelScrollPanelIndex;
                    LevelConfig.levelNumIndex = levelId;
                    LevelConfig.ENTERGAME = LevelConfig.PLAY;
                    SequenceAction sequence = Actions.sequence(
                            Actions.alpha(0, 0),
                            Actions.delay(0.333f),
                            Actions.fadeIn(0.4f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    setScreen(new GameScreen(game));
                                }
                            }));
                    new ShadowImageUtils(sequence,
                            MainScreen.this.stage.getRoot());
                }else {  //玩过的
                    showDialog(new BottomPanel(new SignListener() {
                        @Override
                        public void sign(Object object) {
                            if (interNetDialog(levelScrollPanelIndex))return;
                            LevelConfig.levelNum = levelScrollPanelIndex;
                            LevelConfig.levelNumIndex = levelId;
                            int type = -1;
                            if (object instanceof Integer) {
                                type = (Integer) object;
                            }
                            if (type == 0) {
                                LevelConfig.ENTERGAME = LevelConfig.PLAY;

                                LevelOrder levelOrder1 = InitCsvData.orderHashMap.get(LevelConfig.levelNum);
                                FlurryUtils.uiRestart(levelOrder1.getLevel_id());
                                SequenceAction sequence = Actions.sequence(
                                        Actions.alpha(0, 0),
                                        Actions.delay(0.333f),
                                        Actions.fadeIn(0.4f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                setScreen(new GameScreen(game));
                                            }
                                        }));
                                new ShadowImageUtils(sequence,
                                        MainScreen.this.stage.getRoot());


//                                rootView.addAction(Actions.sequence(Actions.delay(0.333f,Actions.fadeOut(0.2f))
//                                ,Actions.run(()->{
//                                        })));

                            } else if (type == 1) { //delete resource   no vali
                                if (!LevelConfig.useInocal) {
                                    FileHandle local = Gdx.files.local("level/level" + levelId);
                                    for (FileHandle fileHandle : local.list()) {
                                        fileHandle.delete();
                                    }
                                    local.delete();
                                }
                            } else if (type == 2) {
                                LevelConfig.ENTERGAME = LevelConfig.LOOK;
                                SequenceAction sequence = Actions.sequence(
                                        Actions.alpha(0, 0),
                                        Actions.delay(0.333f),
                                        Actions.fadeIn(0.4f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                setScreen(new GameScreen(game));
                                            }
                                        }));
                                new ShadowImageUtils(sequence,
                                        MainScreen.this.stage.getRoot());
//                                setScreen(new GameScreen(game));
                            }
                        }
                    }));
                }
            }

            @Override
            public void addTask(int levelId, LevelItemGroup group) {
                //the last put the frist
                prePicAllTask.put(levelId,group);
                prePicAllTaskIndex.put(group.getLevelNum(),levelId);
            }
        });
        levelScrollPanelIndex++;

        levelItemGroup.setAnimation(stage);

        if (addActorTemp) {
            int i = (LevelConfig.levelNum ) / rowNum;
            if (i * 2 - rowNum <= levelScrollPanelIndex)
            animationArrayActor.add(levelItemGroup);
        }

        return levelItemGroup;
    }

    private boolean interNetDialog(int levelScrollPanelIndex) {
        //定位
        if (!GameStaticInstance.gameListener.isNetConnect()) {
            int base = ArtPuzzlePreferece.getInstance().getCurrentLevel();
            if (levelScrollPanelIndex>base-4 && base+5>levelScrollPanelIndex){
                //file exist?
                LevelOrder levelOrder = InitCsvData.orderHashMap.get(levelScrollPanelIndex);
                String path = StringUtils.append(
                        levelOrder.getVersion(),
                        "/level/level",
                        levelOrder.getLevel_num()
                        );
                FileHandle local = Gdx.files.local(path);
                if (local.exists()) {
                    boolean check = PackZip.check(local);
                    if (check) {
                        return false;
                    }
                }
            }
            showDialog(new NoInterNetDialog());
            return true;
        }else {
            LevelConfig.noInternetLevel = -1;
        }
        return false;
    }


    private boolean adding;

    private void bottomView() {

    }

    private Array<LevelItemGroup> showImage = new Array<>();

    @Override
    public void render(float delta) {
        super.render(delta);
        if (showImage.size>0){
            LevelItemGroup levelItemGroup = showImage.removeIndex(0);
            levelItemGroup.showImage();
        }
        if (isBusy)return;
        if (prePicTask.size>0){
            isBusy = true;
            Integer keyAt = prePicTask.getKeyAt(0);
            prePicAllTask.removeKey(keyAt);
            LevelItemGroup group = prePicTask.removeKey(keyAt);
            prePicAllTaskIndex.removeKey(group.getLevelNum());
            NLog.i("down load index"+keyAt);
            DownLoadPreUtils utils = new DownLoadPreUtils(game, keyAt,group.getVersion(), new Runnable() {
                @Override
                public void run() {
                    isBusy = false;
                    if (group == null){
                        NLog.e("group is null  !");
                    }else {
                        showImage.insert(0, group);
                    }
//                    group.showImage();
                }
            }, new Runnable() {
                @Override
                public void run() {
                    isBusy = false;
                    if (group!=null) {
                        prePicTask.put(keyAt, group);
                    }
                }
            });
            utils.downLoad();
        }
    }

    public void test(){
//        String append = StringUtils.append("level/level", 20+"");
//        SpineActor spineAnimation = new SpineActor(append+ "/finish",append+"/levelAtlas.atlas");
//        addActor(spineAnimation);
//        spineAnimation.setPosition(400,400);
//        ShaderImage2 shaderImage2 = new ShaderImage2(new SpriteDrawable(
//                new Sprite(Asset.getAsset().getTexture("level1.png"))));
//        addActor(shaderImage2);
//        shaderImage2.setShaderType(ShaderType.line);
//        shaderImage2.setAnimation();


//        SpineActor spineActor = new SpineActor("spine/1_3_setting");
//        addActor(spineActor);


    }

    @Override
    protected BaseDialog back() {
//        dialogManager.getBack() instanceof RateD
        BaseDialog back = super.back();
        if (back == null){
            GameStaticInstance.gameListener.home();
        }
        return back;
    }

}
