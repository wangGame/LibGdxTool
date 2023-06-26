//package kw.artpuzzle.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Touchable;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
//import com.badlogic.gdx.utils.Align;
//import com.badlogic.gdx.utils.ArrayMap;
//import com.kw.gdx.BaseGame;
//import com.kw.gdx.asset.Asset;
//import com.kw.gdx.constant.Constant;
//import com.kw.gdx.resource.annotation.ScreenResource;
//import com.kw.gdx.screen.BaseScreen;
//import com.kw.gdx.utils.ads.BannerView;
//
//import kw.artpuzzle.constant.LevelConfig;
//import kw.artpuzzle.downLoad.DownLoadPreUtils;
//import kw.artpuzzle.group.LevelItemGroup;
//import kw.artpuzzle.group.MyScrollPane;
//import kw.artpuzzle.pref.ArtPuzzlePreferece;
//
//@ScreenResource("cocos/mainView.json")
//public class MainScreen1 extends BaseScreen {
//    private boolean isBusy;
//    private Image floatPanel;
//    private Group flatGroup;
//    private float offSetUp = 100;
//    private float floatHight = 300;
//    private ArrayMap<Integer,LevelItemGroup> hashSet = new ArrayMap<>();
//    private int levelScrollPanelIndex = 1;
//    private int maxLevelScrollPanelIndex = 100;
//
//    public MainScreen1(BaseGame game) {
//        super(game);
//    }
//
//    @Override
//    public void initView() {
//        super.initView();
//        initTopView();
//        bottomView();
//        test();
//    }
//
//    private void initTopView() {
//        Table scrollPanelTable = new Table(){{
//            for (int i = 0; i < 2; i++) {
//                Group groupEmpty = new Group();
//                add(groupEmpty);
//                groupEmpty.setSize(100,floatHight);
//            }
//            row();
//        }};
//        int initNum = LevelConfig.levelNum/2 + 5;
//        for (int i = 0; i < initNum; i++) {
//            addPanel(scrollPanelTable);
//        }
//        MyScrollPane middleScrollPanel = new MyScrollPane(scrollPanelTable,new ScrollPane.ScrollPaneStyle()){
//            @Override
//            public void act(float delta) {
//                super.act(delta);
//                float scrollPercentY = getVisualAmountY();
//                if (scrollPercentY < Integer.MAX_VALUE && scrollPercentY > Integer.MIN_VALUE) {
//                    float v = scrollPercentY - lastPosY;
//                    float clamp = MathUtils.clamp(floatPanel.getY() + v, 0, flatGroup.getHeight() + offSetUp);
//                    floatPanel.setY(clamp);
//                    lastPosY = scrollPercentY;
//                    if (scrollPercentY < 1) {
//                        floatPanel.setY(0);
//                    }
//                }
//
//                if (!adding&&getScrollPercentY()>0.9F){
//                    adding = true;
//                    addPanel(scrollPanelTable);
//                }
//            }
//        };
//
//
//
//
//        middleScrollPanel.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT-250 - BannerView.pxToDp(50));
//
//
//        middleScrollPanel.validate();
//        int i = ArtPuzzlePreferece.getInstance().getCurrentLevel() / 2;
//        if (LevelConfig.MAININITPOS==LevelConfig.MAININITPOS_CURRENT_LEVEL) {
//            i = LevelConfig.levelNum / 2;
//        }
//        middleScrollPanel.setScrollY(i*276+floatHight-middleScrollPanel.getHeight()/2);
//        middleScrollPanel.updateVisualScroll();
//
//
//        addActor(middleScrollPanel);
//        middleScrollPanel.setY(Constant.GAMEHIGHT/2 + 50, Align.center);
//        flatGroup = new Group(){
//            @Override
//            public void draw(Batch batch, float parentAlpha) {
//                batch.flush();
//                if (clipBegin(getX(),getY(),getWidth(),getHeight())) {
//                    super.draw(batch, parentAlpha);
//                    batch.flush();
//                    clipEnd();
//                }
//            }
//        };
//
//        addActor(flatGroup);
//        flatGroup.setSize(Constant.GAMEWIDTH,floatHight);
//        flatGroup.setY(middleScrollPanel.getY(Align.top),Align.top);
//        flatGroup.setTouchable(Touchable.childrenOnly);
//        floatPanel = new Image(Asset.getAsset().getTexture("loading/icon.png"));
//        floatPanel.setWidth(Constant.GAMEWIDTH);
//        floatPanel.setHeight(floatHight);
//        flatGroup.addActor(floatPanel);
//        floatPanel.addListener(new ActorGestureListener(){
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                super.pan(event, x, y, deltaX, deltaY);
//                middleScrollPanel.setScrollY(middleScrollPanel.getScrollY()+deltaY);
//            }
//        });
//    }
//
//
//    private void addPanel(Table levelItemTable) {
//        if (levelScrollPanelIndex >maxLevelScrollPanelIndex)return;
//        levelItemTable.add(createItemGroup()).padLeft(-20).padTop(20);
//        levelItemTable.add(createItemGroup()).padLeft(20).padTop(20);
//        levelItemTable.row();
//        levelItemTable.pack();
//        adding = false;
//    }
//
//    private LevelItemGroup createItemGroup() {
//
//        LevelItemGroup levelItemGroup = new LevelItemGroup(levelScrollPanelIndex,, new LevelItemGroup.ItemListener() {
//            @Override
//            public void openLevel(int levelId, int status) {
////                if (status == LevelConfig.PLAYED){
////                    LevelConfig.gameStatus = status;
////                }else {
//                LevelConfig.levelNum = levelId;
////                    if (ArtPuzzle.gameListener.isNetConnect()||checkLevel()){
//                setScreen(new GameScreen(game));
////                    }else {
////                        NLog.e("need connect net");
////                    }
////                }
//            }
//
//            @Override
//            public void addTask(int levelId, LevelItemGroup group) {
//                hashSet.put(levelId, group, 0);
//            }
//        });
//        levelScrollPanelIndex++;
//        return levelItemGroup;
//    }
//
//    public boolean checkLevel(){
//        FileHandle mdFiveFile = Gdx.files.local(LevelConfig.levelDirPath + LevelConfig.levelNum+LevelConfig.mdFileName);
//        if (LevelConfig.useInocal){
//            mdFiveFile = Gdx.files.internal(LevelConfig.levelDirPath + LevelConfig.levelNum+LevelConfig.mdFileName);
//        }
//        if (!mdFiveFile.exists()) {
//            if (LevelConfig.useInocal){
//                BaseGame.setText("file not exist!");
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private boolean adding;
//    private float lastPosY = 0;
//
//    private void bottomView() {
//
//    }
//
//    @Override
//    public void render(float delta) {
//        super.render(delta);
//        if (isBusy)return;
//        if (hashSet.size>0){
//            isBusy = true;
//            Integer keyAt = hashSet.getKeyAt(0);
//            LevelItemGroup group = hashSet.removeKey(keyAt);
//            DownLoadPreUtils utils = new DownLoadPreUtils(game, keyAt, new Runnable() {
//                @Override
//                public void run() {
//                    isBusy = false;
//                    group.showImage();
//                }
//            }, new Runnable() {
//                @Override
//                public void run() {
//                    isBusy = false;
//                    hashSet.put(keyAt,group);
//                }
//            });
//            utils.downLoad();
//        }
//    }
//
//    public void test(){
////        String append = StringUtils.append("level/level", 5+"");
////        Texture texture = Asset.getAsset().getLocalAssetManager(append);
//
////        SpineActor spineAnimation = new SpineActor(append+ "/finish",append+"/levelAtlas.atlas");
////        addActor(spineAnimation);
////        spineAnimation.setPosition(400,400);
//    }
//}
