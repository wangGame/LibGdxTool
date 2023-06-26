package kw.artpuzzle.level;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.animation.effect.EffectTool;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.framebuffer.BufferBufferUtils;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.kw.gdx.resource.csvanddata.demo.CsvUtils;
import com.kw.gdx.sound.AudioProcess;
import com.kw.gdx.sound.AudioType;
import com.kw.gdx.utils.ads.BannerView;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.utils.log.StringUtils;

import java.util.Comparator;
import java.util.HashSet;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.asset.AudioRes;
import kw.artpuzzle.asset.SpineRes;
import kw.artpuzzle.asset.UnloadingFile;
import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.csv.InitCsvData;
import kw.artpuzzle.csv.LevelBean;
import kw.artpuzzle.flurry.FlurryUtils;
import kw.artpuzzle.group.GrayImage2;
import kw.artpuzzle.group.GrayImage3;
import kw.artpuzzle.group.ImageActor;
import kw.artpuzzle.group.LevelPicGroup;
import kw.artpuzzle.group.LevelPicLayout;
import kw.artpuzzle.group.ProcessUtils;
import kw.artpuzzle.group.ShaderImage2;
import kw.artpuzzle.history.LevelHistoryUtils;
import kw.artpuzzle.history.bean.HistoryBean;
import kw.artpuzzle.history.bean.HistoryNum;
import kw.artpuzzle.listener.MyClickListener;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.artpuzzle.screen.GameScreen;
import kw.artpuzzle.screen.MainScreen;
import kw.artpuzzle.screen.Rate01Screen;
import kw.artpuzzle.screen.Rate03Screen;
import kw.artpuzzle.scroll.ScrollPane;
import kw.artpuzzle.shadermanager.ShaderType;
import kw.artpuzzle.signlistener.SignListener;
import kw.artpuzzle.theme.GameTheme;

public class LevelView extends Group {
    private ArrayMap<Integer, Array<String>> everyCengResourceMap;
    private ArrayMap<Integer, Integer> everyCengResourceNumMap;
    private ArrayMap<String, Vector2> everySpritePosMap;
    private ArrayMap<Integer, String> tipLineMap;
    private Vector2 tempVector ;
    private boolean isTouchDrag;
    private int levelCengNum = 1;
    private LevelPicGroup levelPicGroup;
    private ScrollPane bottomScrollPane;
    private Group rootView;
    //用来存储历史使用
    private Array<String> successPic;
    private boolean currentClickSuccess;
//    private Group topProcessGroup;
    private ProcessUtils utils;
    private int allCount = 0;
    private int cengshu = 0;
    private int currentCount = 0;
    private Runnable runnable;
    private ArrayMap<String,LevelBean> allResource;
    private Array<LevelBean> disposeTempLevelBean = new Array<>();
    private boolean tipEnable = false;
    private int currentLevel;


    public LevelView(Group rootView,Runnable runnable) {
        this.runnable = runnable;
        this.currentLevel = LevelConfig.levelNum;
        this.tempVector = new Vector2();
        this.successPic = new Array<>();
        this.allResource = new ArrayMap<>();
        this.rootView = rootView;
        LevelConfig.hintTime = 30;
        LevelConfig.enterNum++;
        LevelConfig.gameCurrentLevelPlayTime = 0;
        FlurryUtils.levelId(InitCsvData.orderHashMap.get(LevelConfig.levelNum).getLevel_id());
        FlurryUtils.gameId(LevelConfig.levelNum);
        if (LevelConfig.levelNum % 10 == 0) {
            if (LevelConfig.levelNum == ArtPuzzlePreferece.getInstance().getCurrentLevel()) {
                HashSet<String> hashSet = LevelHistoryUtils.readFlurryTotal();
                if (!hashSet.contains(LevelConfig.levelNum+"")) {
                    FlurryUtils.total("levelStart-total-"+LevelConfig.levelNum);
                    LevelHistoryUtils.saveFlurryTotal();
                }
            }
        }

        setSize(Constant.GAMEWIDTH, Constant.GAMEHIGHT - BannerView.pxToDp(50) + 38 - 124);
        setTouchable(Touchable.childrenOnly);
        setPosition(Constant.GAMEWIDTH / 2, Constant.GAMEHIGHT / 2, Align.center);
        initGlobelScale();
        //大于  等于  5
        if (ArtPuzzlePreferece.getInstance().getCurrentLevel()>=5){
            //相隔20s
            if (LevelConfig.ENTERGAME == LevelConfig.PLAY) {
                if (LevelConfig.countTime > LevelConfig.interstitial) {
                    GameStaticInstance.gameListener.showInterstitial(2000L);
                }
            }
        }
        addAction(Actions.delay(2,Actions.run(()->{
            tipEnable = true;
        })));
    }

    private void initGlobelScale() {
        float v = Constant.GAMEHIGHT - BannerView.pxToDp(50) - 284.0F - 24 - 22;
        float scale = v / 916.0F;
        if (scale > 0.941048) {
            scale = 0.941048F;
        }
        LevelConfig.globalScale = scale;
    }

    public void initView() {
        LevelConfig.enterAni = (LevelConfig.levelNum-1) % 5;
        LevelConfig.lineType = ShaderType.line1;
        if (LevelConfig.enterAni == 0||
                LevelConfig.enterAni == 1
        ){
//            LevelConfig.lineType = ShaderType.line;
        }else if (LevelConfig.enterAni == 2||LevelConfig.enterAni == 3){
//            LevelConfig.lineType = ShaderType.line1;
        }

        everySpritePosMap = new ArrayMap<>();
        //eve
        everyCengResourceMap = new ArrayMap<>();
        everyCengResourceNumMap = new ArrayMap<>();
        //ceng  lineResourceName
        tipLineMap = new ArrayMap<>();
        //dir + levelNum + filename    bean
        String version = InitCsvData.orderHashMap.get(LevelConfig.levelNum).getVersion();
        Array<LevelBean> levelBeans;
        if (LevelConfig.levelNum == 1){
            levelBeans = CsvUtils.common(
                    version + "/" + LevelConfig.levelDirPath + LevelConfig.levelNumIndex+ "/config.csv",
                    LevelBean.class, true);
        }else {
            levelBeans = CsvUtils.common(
                    version + "/" + LevelConfig.levelDirPath + LevelConfig.levelNumIndex+ "/config.csv",
                    LevelBean.class, LevelConfig.useInocal);
        }
        HashSet<String> strings = new HashSet<>();
//        HashMap<Integer,Integer> everyCengNum = new HashMap<>();
        int typeNum = 0;
        for (LevelBean levelBean : levelBeans) {
            if (levelBean.getHintline() != null && !levelBean.getHintline().trim().equals("")) {
                strings.add(levelBean.getHintline());
                int num = 1;
                if (everyCengResourceNumMap.containsKey(levelBean.getType())){
                    num = everyCengResourceNumMap.get(levelBean.getType()) + 1;
                }else {
                    num = 1;
                }
                everyCengResourceNumMap.put(levelBean.getType(),num);
                allResource.put(levelBean.getResourceName(),levelBean);
            }
            if (levelBean.getType() == 0) {
                typeNum ++;
            } else {
                if (levelBean.getHintline() != null && !levelBean.getHintline().trim().equals(""))
                    tipLineMap.put(levelBean.getType(), levelBean.getHintline());
            }
        }




        cengshu = strings.size();
        allCount = levelBeans.size - typeNum;
        currentCount = 0;
        //有历史
        HistoryBean historyBean = LevelHistoryUtils.readHistory();
        Array<LevelBean> tempDelete = new Array<>();
        if (historyBean != null) {
            Array<String> alreadyPic = historyBean.getAlreadyPic();
            for (LevelBean levelBean : levelBeans) {
                String resourceName = levelBean.getResourceName();
                if (alreadyPic.contains(resourceName, false)) {
                    tempDelete.add(levelBean);
                }
            }
        }
        for (LevelBean levelBean : tempDelete) {
            successPic.add(levelBean.getResourceName());
            levelBeans.removeValue(levelBean, false);
            currentCount++;
        }
        //all pic
        ArrayMap<Integer, LevelBean> allPicName = new ArrayMap();
        //end show ,something pic show ,then display pics;
        ArrayMap<Integer, Array<String>> endResource = new ArrayMap<>();
        deviceData(levelBeans, allPicName, endResource);
//        for (int i = 0; i < everyCengResourceMap.size; i++) {
//            Integer keyAt = everyCengResourceMap.getKeyAt(i);
////            everyCengResourceNumMap.put(keyAt,everyCengResourceMap.get(keyAt).size);
//        }
        initLevelPicView();
        check01(levelBeans);
        everySpritePos(allPicName);
        hideAllSprite();
        if (selectCengShu()) return;
        topProcess(strings,levelCengNum);
        String fristCeng = tipLineMap.get(levelCengNum);

        ShaderImage2 fristCengActor = levelPicGroup.findActor(fristCeng);
        fristCengActor.addAction(Actions.delay(1.5f, Actions.run(() -> {
            fristCengActor.setAnimation();
            fristCengActor.setVisible(true);
        })));
        levelPicLayout = setBottomTable(allPicName, endResource);
        showBottom(levelPicLayout);
        bottomLabel();
        Array<String> array = everyCengResourceMap.get(levelCengNum);
        Integer integer = everyCengResourceNumMap.get(levelCengNum);
        utils.setProcess((integer-array.size)*1.0f/integer,false);

//        levelPicGroup.addAction(Actions.sequence(
//                Actions.scaleTo(LevelConfig.globalScale - 0.2f,LevelConfig.globalScale - 0.2f,0),
//                    Actions.scaleTo(LevelConfig.globalScale,LevelConfig.globalScale,0.4333f,new BseInterpolation(
//                            0,0,0.143f,1.0f
//                    ))
//                ));
    }

    private Label bottom_pack_num;

    private void bottomLabel() {
        Group game_bottom = rootView.findActor("game_bottom");
        bottom_pack_num = game_bottom.findActor("bottom_pack_num");
        bottom_pack_num.setAlignment(Align.center);
        bottom_pack_num.setText(currentCount + "/" + allCount);
    }

    public int getCurrentCount() {
        return currentCount;
    }

    private void update() {
        bottom_pack_num.setText(currentCount + "/" + allCount);
    }

    private void topProcess(HashSet<String> strings, int levelCengNum) {
        Group game_top = rootView.findActor("game_top");
        Image top_progress1 = game_top.findActor("top_progress");
        top_progress1.setColor(Color.BLUE);
        utils = new ProcessUtils(strings.size(),top_progress1);
        utils.setPoint(levelCengNum);

        game_top.addActor(utils);
        utils.setPosition(game_top.getWidth()/2.0f, game_top.getHeight()/2.0f, Align.center);
    }

//    public Group getTopProcessGroup() {
//        return topProcessGroup;
//    }

    private LevelPicLayout levelPicLayout;

    private LevelPicLayout setBottomTable(ArrayMap<Integer, LevelBean> allPicName, ArrayMap<Integer, Array<String>> endResource) {
        LevelPicLayout table = new LevelPicLayout() {
            {
                Group leftGroup = new Group();
                addActor(leftGroup);
                leftGroup.setSize(160 + 100, 100);
                Object[] values1 = allPicName.values;
                Array<LevelBean> tempLevelBean = new Array<>();
                for (int i = 0; i < allPicName.size; i++) {
                    Object obj = values1[i];
                    tempLevelBean.add((LevelBean) obj);
                }
                if (LevelConfig.ISSHUFFLE == LevelConfig.SHUFFLE) {
                    tempLevelBean.sort(new Comparator<LevelBean>() {
                        @Override
                        public int compare(LevelBean o1, LevelBean o2) {
                            return o2.getSort() - o1.getSort();
                        }
                    });
                }
                disposeTempLevelBean.addAll(tempLevelBean);
                for (int i = 0; i < tempLevelBean.size; i++) {
                    LevelBean value = null;
                    if (LevelConfig.ISSHUFFLE == LevelConfig.NISHUFFLE) {
                        value = tempLevelBean.get(tempLevelBean.size - 1 - i);
                    } else {
                        value = tempLevelBean.get(i);
                    }
//                    value = tempLevelBean.get(tempLevelBean.size - 1 - i);
                    String s = value.getResourceName();
                    if (tipLineMap.containsValue(s, false)) continue;
                    if (value.getType() == 0) continue;
                    TextureAtlas atlas;
                    String version = InitCsvData.orderHashMap.get(LevelConfig.levelNum).getVersion();
                    if (LevelConfig.levelNum == 1){
                        atlas = Asset.getAsset().getAtlas(version + "/" + LevelConfig.levelDirPath + LevelConfig.levelNumIndex + "/" + "levelAtlas.atlas");
                    }else {
                        if (LevelConfig.useInocal) {
                            atlas = Asset.getAsset().getAtlas(version + "/" + LevelConfig.levelDirPath + LevelConfig.levelNumIndex + "/" + "levelAtlas.atlas");
                        } else {
                            atlas = Asset.getAsset().getLocalAtlas(version + "/" + LevelConfig.levelDirPath + LevelConfig.levelNumIndex + "/" + "levelAtlas.atlas");
                        }
                    }

                    ImageActor item = new ImageActor(new SpriteDrawable(atlas.createSprite(s)), s) {
                        @Override
                        protected void sizeChanged() {
                            super.sizeChanged();
                            peak();
                            if (bottomScrollPane != null) {
                                bottomScrollPane.layout();
                            }
                        }
                    };
                    Array<String> afterShow = getEveryCengResourceArray(endResource, value.getId());
                    item.setAfterShow(afterShow);
                    addActor(item);
                    item.setListener(gameItemListener(item));
                }
                Group rightGroup = new Group();
                addActor(rightGroup);
                rightGroup.setSize(50, 100);
//            peak();
                animation();
            }
        };
        return table;
    }

    private void showBottom(LevelPicLayout table) {
        bottomScrollPane = new ScrollPane(table, new ScrollPane.ScrollPaneStyle());
        bottomScrollPane.addAction(new Action() {
            @Override
            public boolean act(float v) {
                if (bottomScrollPane.getFlingTimer()>0){
                    if (handImage!=null){
                        handImage.addAction(Actions.fadeOut(0.4f));
                    }
                }
                return false;
            }
        });

      //  bottomScrollPane.setOverscroll(false,false);
        table.setScrollPane(bottomScrollPane);
        addActor(bottomScrollPane);
        bottomScrollPane.setFlingTime(1f);
        bottomScrollPane.setSize(Constant.GAMEWIDTH +440, Constant.GAMEHIGHT);
        bottomScrollPane.setCancelTouchFocus(false);
        bottomScrollPane.setPosition(getWidth() / 2, getHeight()/2, Align.center);
//        bottomScrollPane.setOverscroll(true, false);
        bottomScrollPane.setupOverscroll(200, 500, 1000);
        bottomScrollPane.setRectangle(0,124);
    }

    private void deviceData(Array<LevelBean> levelBeans,
                            ArrayMap<Integer, LevelBean> allPicName,
                            ArrayMap<Integer, Array<String>> endResource) {
        for (LevelBean levelBean : levelBeans) {
            allPicName.put(levelBean.getId(), levelBean);
            if (levelBean.getType() == 0) {
                int direct = levelBean.getDirect();
                Array<String> strings = getEveryCengResourceArray(endResource, direct);
                if (strings == null) {
                    Array<String> array = new Array<>();
                    array.add(levelBean.getResourceName());
                    endResource.put(direct, array);
                } else {
                    strings.add(levelBean.getResourceName());
                }
            } else {

//
                if (everyCengResourceMap.containsKey(levelBean.getType())) {
                    Array<String> levelBeans1 = getEveryCengResourceArray(everyCengResourceMap, levelBean.getType());
                    levelBeans1.add(levelBean.getResourceName());
                } else {
                    Array<String> levelBeans1 = new Array<>();
                    levelBeans1.add(levelBean.getResourceName());
                    everyCengResourceMap.put((int)levelBean.getType(), levelBeans1);
                }
            }
        }
    }

    private void hideAllSprite() {
        Object[] objects = tipLineMap.values;
        for (int i = 0; i < tipLineMap.size; i++) {
            String s = (String) objects[i];
            ShaderImage2 actor = levelPicGroup.findActor(s);
            actor.setShaderType(LevelConfig.lineType);
            levelPicGroup.findActor(s).setVisible(false);
        }
    }

    private void everySpritePos(ArrayMap<Integer, LevelBean> allPicName) {
        //
        Object[] values = allPicName.values;
        for (int i = 0; i < allPicName.size; i++) {
            Object obj = values[i];
            LevelBean value = (LevelBean) obj;
            String child = value.getResourceName();
            LevelBean levelBean = allResource.get(child);
            ShaderImage2 actor = levelPicGroup.findActor(child);
            if (actor == null) {
                NLog.e("%s not found !", child);
                continue;
            }
            Vector2 vector2 = new Vector2(actor.getX(Align.center), actor.getY(Align.center));
            everySpritePosMap.put(child, vector2);
            if (levelBean!=null){
                actor.setShaderByUser(levelBean.getInput());
            }
            actor.setVisible(false);
        }
    }

    private void initLevelPicView() {
        //dir + levelNum
        String version = InitCsvData.orderHashMap.get(LevelConfig.levelNum).getVersion();
        levelPicGroup
                = new
                LevelPicGroup(version + "/"+ LevelConfig.levelDirPath + LevelConfig.levelNumIndex + "/"
                , tipLineMap);

        addActor(levelPicGroup);


        Actor img_border = rootView.findActor("img_border");
        levelPicGroup.addActor(img_border);
        img_border.setColor(Color.valueOf("#f5ecd0"));
        img_border.setSize(levelPicGroup.getWidth()+2,levelPicGroup.getHeight()+1);
        img_border.setPosition(levelPicGroup.getWidth()/2,
                levelPicGroup.getHeight()/2,Align.center);


        levelPicGroup.setOrigin(Align.center);
        levelPicGroup.setPosition(getWidth() / 2, getHeight() / 2 + 100, Align.center);
        //bg enter animation
        levelPicGroup.bgAnimation();
        levelPicGroup.setOrigin(Align.center);
        levelPicGroup.setScale(LevelConfig.globalScale);
    }

    private void check01(Array<LevelBean> levelBeans) {
        for (LevelBean levelBean : levelBeans) {
            Actor actor = levelPicGroup.findActor(levelBean.getResourceName());
            if (actor == null) {
                NLog.i(levelBean.getResourceName() + "   csv difference json!!!");
            }
        }
    }

    private boolean selectCengShu() {
        while (true) {
            if (everyCengResourceMap.containsKey(levelCengNum)) {
                break;
            }
            levelCengNum++;

            if (levelCengNum > 40) {
                NLog.e("wang ge,There seems to be a problem! find ceng num %s ?", levelCengNum);
                return true;
            }
        }
        return false;
    }


    private boolean gameTouchDown = false;
    private boolean errorCancel;

    private Actor currentTouchDown;
    private Actor oldTouchDown;


    private MyClickListener gameItemListener(ImageActor item) {
        return new MyClickListener(item) {
            private Vector2 userTouchDownPos = new Vector2();
            private Vector2 userDragPos = new Vector2();
            private Vector2 spriteOffsetDistance = new Vector2(0, 200);
            private Vector2 stageVector = new Vector2(0,0);
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isTouchDrag = false;
                gameTouchDown = false;
                NLog.e("click debug touch up --------------------");
                if (currentClickSuccess) {
                    return;
                }

                currentTouchDown = null;

                stageVector.set(event.getStageX(),event.getStageY());
                try {

                    getTargetActor().resetPosition();
                    getTargetActor().addRestAction();

                }catch (Exception e){
                    NLog.e("???????????? ");
                }

                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("click debug touch down -------------"+pointer);
                if (pointer==2){
                    if (errorCancel)return false;
                    errorCancel = true;
//                    System.out.println("click debug touch cancel --------------------");

//                    if (currentClickSuccess) {
////                        return;
//                    }
//                    cancel();
                    getTargetActor().resetPosition();
                    getTargetActor().addRestAction();
                }
                if (pointer>0)return false;

                if (gameTouchDown)return false;


                currentTouchDown = getTargetActor();

                errorCancel = false;
//                System.out.println("click debug touch down 111111111--------------------");
                gameTouchDown = true;
                isTouchDrag = false;
                bottomScrollPane.setValid(true);
                userTouchDownPos.set(x, y);
                stageVector.set(event.getStageX(),event.getStageY());
                boolean touchDown = super.touchDown(event, x, y, pointer, button);
                if (!touchDown) {
                    currentClickSuccess = false;
                    super.touchDown(event, x, y, pointer, button);
                }
                return touchDown;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (errorCancel){
                    getTargetActor().resetPosition();
                    getTargetActor().addRestAction();
                    return;
                }
                if ((Math.abs(event.getStageX() - stageVector.x)+Math.abs(event.getStageY() - stageVector.y))>10) {
                    if (handImage != null) {
                        handImage.addAction(Actions.fadeOut(0.2f));
                        handImage.remove();
                        nextGuideAble = true;
                    }
                }
                if (!isTouchDrag) {
                    if ((Math.abs(event.getStageX() - stageVector.x)+Math.abs(event.getStageY() - stageVector.y))<10)return;
                    float absX = Math.abs(event.getStageX() - stageVector.x);
                    float absY = Math.abs(event.getStageY() - stageVector.y);
                    if (absX  > absY* 2f) {
                        NLog.e(absX * 1.5f +"    "+absY);
                        return;
                    }
                    if (event.getStageY() - stageVector.y < 0) return;
                }
                if (isAllowDrag()) return;

                super.touchDragged(event, x, y, pointer);
                ImageActor imageActor = getTargetActor();
                if (imageActor.isAnimation()) return;
                dragAnimation(x, y, imageActor);
                Vector2 currentPos = new Vector2(x, y);
                userDragPos.set(imageActor.getLevelImageX(), imageActor.getLevelImageY());
                userDragPos.add(currentPos);
                userDragPos.add(spriteOffsetDistance);
                imageActor.setLevelImagePosition(userDragPos.x, userDragPos.y - userTouchDownPos.y);
//                NLog.e("--------   %s ---------,--------   %s -------",imageActor.getX(),imageActor.getY());
                if (check(imageActor) && !imageActor.isAnimationStart()) {
                    if (tipAction!=null) {
                        if (tipAction.getActor()!=null) {
                            if (handImage!=null) {
                                handImage.addAction(Actions.fadeOut(0.2f));
                                nextGuideAble = true;
                            }
                            tipAction.getActor().removeAction(tipAction);
                        }
                        tipAction = null;
                    }

                    imageActor.setAnimationStart(false);
                    currentClickSuccess = true;
                    successPic.add(imageActor.getName());
                    ShaderImage2 actor = levelPicGroup.findActor(imageActor.getName());
                    setLevelImagePos(actor);
                    actor.getParent().localToStageCoordinates(tempVector);
                    imageActor.getLevelImage().getParent().stageToLocalCoordinates(tempVector);
                    String name = imageActor.getName();
                    imageActor.getHintGroup().clearListeners();
                    imageActor.getHintGroup().clearActions();
                    Array<String> afterShow = imageActor.getAfterShow();
                    if (afterShow!=null) {
                        for (String s : afterShow) {
                            try {
                                levelPicGroup.findActor(s).addAction(Actions.fadeIn(0.2f));
                            }catch (Exception e){
                                NLog.e("not found actor "+s);
                            }
                        }
                    }
                    LevelConfig.hintTimeCount = 0;
                    LevelConfig.hintTime = 30;
                    imageActor.getLevelImage().setOrigin(Align.center);
                    final int tempCeng = levelCengNum;
                    imageActor.getLevelImage().addAction(
                            Actions.sequence(
                                    Actions.moveToAligned(tempVector.x,
                                            tempVector.y, Align.center, 0.1F),
                                    Actions.parallel(
                                            Actions.run(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AudioProcess.playSound(AudioType.inputA);
                                                    int v = (int) (Math.random() * 2);
                                                    imageActor.shanbai(v);
                                                    GameStaticInstance.gameListener.vibrate(25, -1);
                                                }
                                            })),
                                    Actions.scaleTo(1.05f, 1.05f, 0.2f),  //scale
                                    Actions.parallel(
                                            Actions.scaleTo(1f, 1f, 0.2f),
                                            Actions.fadeOut(0.2F)),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            bottomScrollPane.setValid(true);
                                            updateBottomSize();

                                            Array<String> array = everyCengResourceMap.get(tempCeng);
                                            Integer integer = everyCengResourceNumMap.get(tempCeng);
                                            if (utils!=null) {
                                                if (array != null){
                                                    utils.setProcess((integer-array.size)*1.0f/integer);
                                                }else {
                                                    utils.setProcess(1.0f);
                                                }
                                            }
                                            currentClickSuccess = false;
                                            imageActor.remove();
                                            isTipEnable = true;
                                            if (LevelConfig.guide){
                                                if (everyCengResourceMap.size <= 0 && everySpritePosMap.size >0) {
                                                    addAction(Actions.delay(6, Actions.run(() -> {
                                                        tip();
                                                    })));
                                                }else {
                                                    if (everySpritePosMap.size >0) {
                                                        addAction(Actions.delay(2, Actions.run(() -> {
                                                            tip();
                                                        })));
                                                    }
                                                }
                                            }

                                        }
                                    }))
                    );

                    actor.addAction(Actions.delay(0.3f, Actions.run(() -> {
                        actor.setAnimation();
                        actor.setVisible(true);
                        actor.getColor().a = 0;
                        actor.addAction(Actions.sequence(Actions.alpha(1, 0.6F),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        update();
                                    }
                                })));
                    })));
//                    success();
//                    LevelPicGroup.findActor(name).setVisible(true);
                    if (everySpritePosMap.size <= 0) {
                        success();
                    } else {
                        everySpritePosMap.removeKey(imageActor.getName());
                        Array<String> levelBeans = getEveryCengResourceArray(everyCengResourceMap, levelCengNum);
                        levelBeans.removeValue(name, false);
                        currentCount++;

                        if (levelBeans.size <= 0) {
                            if (everyCengResourceMap.size <= 0) {
                                success();
                            } else {
                                everyCengResourceMap.removeKey(levelCengNum);
                                if (everyCengResourceMap.size <= 0) {
                                    String pre = tipLineMap.get(levelCengNum);
                                    levelPicGroup.findActor(pre).addAction(Actions.sequence(
                                            Actions.delay(1.3f),
                                            Actions.fadeOut(0.4f)));
                                    success();
                                } else {
                                    String pre = tipLineMap.get(levelCengNum);
                                    levelPicGroup.findActor(pre).addAction(Actions.sequence(
                                            Actions.delay(1.3f),
                                            Actions.fadeOut(0.4f),
                                            Actions.run(()->{
                                                //层num此时已经+1了
                                                if (allCount > 40){
                                                    if (cengshu==4) {
                                                        if (levelCengNum == 3) {
                                                            if(LevelConfig.countTime>LevelConfig.interstitial) {
                                                                GameStaticInstance.gameListener.showInterstitial(2000L);
                                                            }
                                                        }
                                                    }else if (cengshu>=5){
                                                        if (levelCengNum == 5) {
                                                            if(LevelConfig.countTime>LevelConfig.interstitial) {
                                                                GameStaticInstance.gameListener.showInterstitial(2000L);
                                                            }
                                                        }
                                                    }
                                                }
                                            }),
                                            Actions.visible(false)
                                            ));
                                    newProcess(levelCengNum);


                                    levelCengNum++;
                                    String cur = tipLineMap.get(levelCengNum);
                                    Actor actor1 = levelPicGroup.findActor(cur);

                                    actor1.addAction(
                                            Actions.sequence(
                                                    Actions.delay(0.3F),
                                                    Actions.parallel(
                                                            Actions.run(() -> {
                                                                if ((actor1 instanceof ShaderImage2)) {
                                                                    ((ShaderImage2) actor1).setAnimation();
                                                                }
                                                            }),
                                                            Actions.sequence(Actions.delay(1.5f),Actions.visible(true))
                                                    )));
                                }
                            }
                        }
                    }
                }
            }

            private void newProcess(final int levelCengNumTemp) {
                addAction(Actions.delay(1.5f,Actions.run(()->{
                    utils.qieHuanEnd1(levelCengNumTemp);
                    Array<String> array = everyCengResourceMap.get(levelCengNum);
                    Integer integer = everyCengResourceNumMap.get(levelCengNum);
                    utils.setProcess((integer-array.size)*1.0f/integer);
                })));
            }

            @Override
            public void cancel() {
                super.cancel();
//                System.out.println("click debug touch cancel --------------------");
                if (errorCancel) {

                }
                gameTouchDown = false;
                currentTouchDown = null;
                errorCancel = true;

                if (currentClickSuccess) {
                    return;
                }
//                System.out.println("click debug touch cancel --------------------");
                getTargetActor().cancel();
            }
        };
    }

    public void updateBottomSize() {
        levelPicLayout.upDataWidth();
    }

    private void dragAnimation(float x, float y, ImageActor imageActor) {
        if (x > 30 || y > 30) {
            if (!isTouchDrag) {
                isTouchDrag = true;
                bottomScrollPane.setValid(false);
                imageActor.startDragAnimation();
                bottomScrollPane.cancel();
            }
        }
    }

    private boolean isAllowDrag() {
        if (currentClickSuccess) {
            return true;
        }

        return false;
    }
    private Vector2 tem1 = new Vector2();

    public boolean check(ImageActor actor) {
        if (actor == null) return false;
        if (actor.getParent() == null) return false;
        String name = actor.getName();
        Array<String> everyCengResourceArray = getEveryCengResourceArray(this.everyCengResourceMap, levelCengNum);
        if (everyCengResourceArray.contains(name, false)) {
            //找到目标位置
            Vector2 spritePosition = findSpritePostion(name);
            if (spritePosition == null) {
                NLog.i(name);
            }
            setLevelImagePos(actor.getLevelImage());

            //            tem1.set(temp);
            actor.getLevelImage().getParent().localToStageCoordinates(tempVector);
            levelPicGroup.stageToLocalCoordinates(tempVector);
//            actor.getLevelImage().getParent().stageToLocalCoordinates(temp);
            float distance = distance(spritePosition, tempVector);
            if (distance < 30) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private Vector2 setLevelImagePos(Image levelImage) {
        return tempVector.set(levelImage.getX(Align.center), levelImage.getY(Align.center));
    }

    private Vector2 setLevelImagePos1(Image levelImage) {
        return tempVector.set(levelImage.getX(Align.center), levelImage.getY(Align.center));
    }

    private Vector2 findSpritePostion(String name) {
        return everySpritePosMap.get(name);
    }

    public Array<String> getEveryCengResourceArray(ArrayMap<Integer, Array<String>> everyCengResourceMap, int levelCengNum) {
        return everyCengResourceMap.get(levelCengNum);
    }

    public float distance(Vector2 ori, Vector2 dst) {
        return ori.dst(dst);
    }


    private void success() {
        if (LevelConfig.guide){
            FlurryUtils.fristEndLoading();
        }


        Group ad = rootView.findActor("ad");
        if (ad!=null){
            ad.setTouchable(Touchable.disabled);
        }


        float gameCurrentLevelPlayTime = LevelConfig.gameCurrentLevelPlayTime;

        String value = "120-";
        if(gameCurrentLevelPlayTime <= 120.0f){
            value = 120+"-";
        }else if (gameCurrentLevelPlayTime >= 600.0f){
            value = 600+"+";
        }else {
            int[] dict = LevelConfig.dict;
            for (int i = 1; i < dict.length; i++) {
                if (dict[i] >= gameCurrentLevelPlayTime) {
                    value = dict[i-1]+"";
                    break;
                }
            }
        }
        FlurryUtils.roundTime("levelExit-round_time-"+value);
        FlurryUtils.levelExitLevelid(InitCsvData.orderHashMap.get(LevelConfig.levelNum).getLevel_id());
        FlurryUtils.levelExitGameid(LevelConfig.levelNum);





        if (LevelConfig.levelNum % 10 == 0) {
            if (LevelConfig.levelNum == ArtPuzzlePreferece.getInstance().getCurrentLevel()) {
                FlurryUtils.levelExitTotal("levelStart-total-"+LevelConfig.levelNum);
            }
        }




        if (!GameStaticInstance.gameListener.isNetConnect()){
            LevelConfig.noInternetLevel = LevelConfig.levelNum;
        }else {
            LevelConfig.noInternetLevel = -1;
        }
        if (LevelConfig.levelNum == 1){
            ArtPuzzlePreferece.getInstance().updateGuide();

        }
        if (LevelConfig.levelNum == ArtPuzzlePreferece.getInstance().getCurrentLevel()) {
            LevelConfig.LOCKANIMATION = true;
        }
        //rate hide ads
        LevelHistoryUtils.deleteFile();
        successPic.clear();

        addAction(Actions.delay(1F, Actions.run(() -> {
            GameStaticInstance.gameListener.vibrate1();
            boolean b = successDisplay(true);

            ArtPuzzlePreferece.getInstance().addLevelNum();
            if (b) {
                Group game_top = rootView.findActor("game_top");
                Actor top_tp_g = game_top.findActor("top_tp_g");
                top_tp_g.addAction(Actions.parallel(
                        Actions.sequence(Actions.delay(0.1333f),Actions.fadeOut(0.13333f)),
                        Actions.sequence(Actions.scaleTo(1.068f,1.068f,0.16667f,
                                new BseInterpolation(0,0.01f,0.075f,1.0f)),
                                Actions.scaleTo(0.509f,0.509f,0.1f,new BseInterpolation(0.25f,0,1,1)))
                        ));

                Actor top_ad_g = game_top.findActor("top_ad_g");
                top_ad_g.addAction(Actions.parallel(
                        Actions.sequence(Actions.delay(0.1667f),Actions.fadeOut(0.13333f)),
                        Actions.sequence(Actions.scaleTo(1.068f,1.068f,0.16667f,
                                        new BseInterpolation(0,0.01f,0.075f,1.0f)),
                                Actions.scaleTo(0.509f,0.509f,0.1f,new BseInterpolation(0.25f,0,1,1)))
                ));



                Actor top_back = game_top.findActor("top_back");
                top_back.addAction(Actions.sequence(
                        Actions.moveTo(top_back.getX()+29,top_back.getY(),0.1667f,
                                new BseInterpolation(0,0,0.75f,1)),
                        Actions.moveTo(top_back.getX()-102.06f,top_back.getY(),0.13333f,
                                new BseInterpolation(0.25f,0,1,1))
                ));
                Group game_bottom = rootView.findActor("game_bottom");
                Actor bottom_bg = game_bottom.findActor("bottom_bg");
                float x = bottom_bg.getX();
                float y = bottom_bg.getY();
                float useTime = 0.2667f * Constant.GAMEWIDTH / 758.73f;
//                bottom_bg.addAction(Actions.moveTo(x+Constant.GAMEWIDTH,y,useTime,new BseInterpolation(0,0,0.75f,1)));
                bottom_bg.addAction(Actions.fadeOut(0.2f));
                rootView.findActor("game_top").addAction(Actions.fadeOut(0.3f));
                Actor bottom_num = game_bottom.findActor("bottom_num");
                float width = bottom_num.getWidth();
                bottom_num.addAction(Actions.moveTo(bottom_num.getX() - width,bottom_num.getY(),0.1F,
                        new BseInterpolation(
                        0.75f,0,1,1
                )));
            }
        })));
    }

    private SpineActor liziAnimation;
    private Group huaKuang ;
    private Group spineAnimation;
    private Image huangBg;
    private SpineActor spineAnimation1;
    public boolean successDisplay(boolean isAnimation) {
        String version = InitCsvData.orderHashMap.get(LevelConfig.levelNum).getVersion();
        String append = StringUtils.append(version + "/level/level", LevelConfig.levelNumIndex);
        FileHandle tarGetFileDir = Gdx.files.local(append + "/finish.json");

        if (LevelConfig.levelNum == 1){
            tarGetFileDir = Gdx.files.internal(append + "/finish.json");
        }else {
            if (LevelConfig.useInocal){
                tarGetFileDir = Gdx.files.internal("/"+append + "/finish.json");
            }else {
                tarGetFileDir = Gdx.files.local("/"+append + "/finish.json");
            }
        }
        if (!tarGetFileDir.exists()) {
            NLog.e("animation not exsit");
            Image image = new Image(Asset.getAsset().getTexture("next.png"));
            addActor(image);
            image.setOrigin(Align.center);
            image.getColor().a = 0;
            image.addAction(Actions.delay(2, Actions.fadeIn(0.3F)));
            image.setPosition(Constant.GAMEWIDTH / 2, 0, Align.center);
            image.addListener(new OrdinaryButtonListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    image.addAction(Actions.sequence(Actions.fadeOut(0.3F),
                            Actions.delay(0.6F),
                            Actions.run(() -> {
                                LevelView.this.dispose();
                                Constant.currentScreen.setScreen(MainScreen.class);
                            })));
                }
            });
            return false;
        }

        AssetManager assetManager = Asset.localAssetManager;
        if (LevelConfig.useInocal){
            assetManager = Asset.assetManager;
        }
        if (LevelConfig.levelNum == 1){
            assetManager = Asset.assetManager;
        }else {
            assetManager = Asset.localAssetManager;
        }

        huaKuang = new Group();
        addActor(huaKuang);
        spineAnimation1 = new SpineActor(append + "/finish",
                  append + "/levelAtlas.atlas",assetManager);
        spineAnimation  = new Group();

        huaKuang.addActor(spineAnimation);
        spineAnimation.setScale(LevelConfig.globalScale);
        spineAnimation.setOrigin(Align.bottomLeft);
        huaKuang.setPosition( getWidth()/2+1, getHeight() / 2 + 100, Align.center);
        huangBg = new Image(Asset.getAsset().getTexture("cocos/finish/paper.png"));
        addActor(huangBg);




        huangBg.getColor().a = 0;
        huangBg.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        huangBg.setPosition(getWidth()/2,getHeight()/2,Align.center);
        spineAnimation.addActor(spineAnimation1);
        spineAnimation1.setVisible(false);

        spineAnimation.addAction(Actions.sequence(
                Actions.delay(0.8F + 1/6.0f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (levelPicGroup!=null) {
                            levelPicGroup.setVisible(false);
                        }
                        spineAnimation1.setVisible(true);

                        AudioProcess.playSound(AudioType.viewA);
                        spineAnimation1.setAnimation("animation", true);
                        liziAnimation = new SpineActor("spine/lizi");
                        huaKuang.addActor(liziAnimation);
                        liziAnimation.setAnimation("animation",true);
                        liziAnimation.setScale(1.16f);
                        liziAnimation.getColor().a= 0;
                        liziAnimation.addAction(Actions.fadeIn(0.3f));
                        liziAnimation.setPosition(huaKuang.getWidth()/2-5, huaKuang.getHeight()/2,Align.center);
                        float scaleX = spineAnimation1.getScaleX();
                        spineAnimation1.addAction(Actions.scaleTo(1.04f * scaleX,1.04f*scaleX,3.5f));
//                        if (isAnimation){
//                            spineAnimation1.getColor().a = 0;
//                            spineAnimation1.addAction(Actions.delay(0.2f,Actions.fadeIn(0.4f)));
//                        }
                    }
                })));

        addAction(Actions.delay(5 +  1/6.0f,Actions.run(()->{
            spineAnimation1.getAnimaState().clearListeners();

            if (LevelConfig.levelNum == 3 && ArtPuzzlePreferece.getInstance().getCurrentLevel() == 3) {
//            if (ArtPuzzlePreferece.getInstance().getCurrentLevel() != 3) {
                if (!ArtPuzzlePreferece.getInstance().isRate()) {
                    ArtPuzzlePreferece.getInstance().saveRate();
                    //弹窗
                    Screen screen = GameStaticInstance.gameInstance.getScreen();
                    if (screen instanceof GameScreen){
                        ((GameScreen)(screen)).getDialogManager().showDialog(new Rate01Screen(new Runnable() {
                            @Override
                            public void run() {
                                scaleSmall();
                            }
                        }));
                    }
                }else {
                    scaleSmall();
                }
            }else {

                if (ArtPuzzlePreferece.getInstance().getCurrentLevel()>5) {
                    //相隔20s
                    if(LevelConfig.countTime>LevelConfig.interstitial) {
                        if (LevelConfig.ENTERGAME == LevelConfig.PLAY) {
                            GameStaticInstance.gameListener.showInterstitial(2000L);
                        }
                    }
                }

                scaleSmall();
            }


        })));
        addAction(new Action() {
            @Override
            public boolean act(float v) {
                ddd += v;
                return false;
            }
        });
//        spineAnimation1.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
//            @Override
//            public void complete(AnimationState.TrackEntry entry) {
//                super.complete(entry);
//                System.out.println(ddd +" ---------------------------- ");
//
//            }
//        });

        SpineActor saoGuang = new SpineActor("spine/saoguang");
        huaKuang.addActor(saoGuang);
        saoGuang.setY(-100);
        saoGuang.setVisible(false);
        addAction(Actions.delay(0.8f + 1/6.0f,Actions.run(()->{
            saoGuang.setAnimation("sg",false);
            saoGuang.setVisible(true);
        })));
        if (isAnimation) {
            successView();
        }else {
            successDisPlay();
        }
//        grayBgImage(append, assetManager);
        return true;
    }

    public void scaleSmall(){
        if (liziAnimation!=null) {
            liziAnimation.remove();
        }


        GameStaticInstance.gameListener.hideBanner();


        successAnimation();
        EffectTool leftCaidailizi = new EffectTool("lizi/baocaidai_2");
        huaKuang.addActor(leftCaidailizi);
        leftCaidailizi.setX(-Constant.GAMEWIDTH/2 - 100);
        leftCaidailizi.setY(-250);


        EffectTool youCaidailizi = new EffectTool("lizi/baocaidai");
        huaKuang.addActor(youCaidailizi);
        youCaidailizi.setX(Constant.GAMEWIDTH/2+ 100);
        youCaidailizi.setY(-250);
    }
    float ddd = 0;

//    public boolean successDisplay() {
//        String append = StringUtils.append("level/level", LevelConfig.levelNum);
//        FileHandle tarGetFileDir = Gdx.files.local(append + "/finish.json");
//        if (LevelConfig.useInocal){
//            tarGetFileDir = Gdx.files.internal(append + "/finish.json");
//        }
//        if (!tarGetFileDir.exists()) {
//            NLog.e("animation not exsit");
//            Image image = new Image(Asset.getAsset().getTexture("next.png"));
//            addActor(image);
//            image.setOrigin(Align.center);
//            image.getColor().a = 0;
//            image.addAction(Actions.delay(2, Actions.fadeIn(0.3F)));
//            image.setPosition(Constant.GAMEWIDTH / 2, 0, Align.center);
//            image.addListener(new OrdinaryButtonListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    super.clicked(event, x, y);
//                    image.addAction(Actions.sequence(Actions.fadeOut(0.3F),
//                            Actions.delay(0.6F),
//                            Actions.run(() -> {
//                                Constant.currentScreen.setScreen(MainScreen.class);
//                            })));
//                }
//            });
//            return false;
//        }
//
//        AssetManager assetManager = Asset.localAssetManager;
//        if (LevelConfig.useInocal){
//            assetManager = Asset.assetManager;
//        }
//
//        huaKuang = new Group();
//        addActor(huaKuang);
//        SpineActor spineAnimation1 = new SpineActor(append + "/finish", append + "/levelAtlas.atlas",assetManager);
//        spineAnimation  = new Group(){
//            @Override
//            public void draw(Batch batch, float parentAlpha) {
//                batch.flush();
//                float width = finishBg.getWidth();
//                float height = finishBg.getHeight();
//
////                spineAnimation.setClip(true);
////                spineAnimation.setBeginX((int) -width+50);
////                spineAnimation.setBeginY((int) -height+50);
////                spineAnimation.setW(width*2);
////                spineAnimation.setH(height*2);
////                if (clipBegin(-width/2 + 35,-height/2+ 35,width-20,height-80)){
//                    super.draw(batch, parentAlpha);
////                    batch.flush();
////                    clipEnd();
////                }
//            }
//        };
//        spineAnimation.addActor(spineAnimation1);
//        huaKuang.addActor(spineAnimation);
//        spineAnimation.setScale(LevelConfig.globalScale);
//        spineAnimation.setOrigin(Align.bottomLeft);
//        huaKuang.setPosition( getWidth()/2+1, getHeight() / 2 + 100, Align.center);
//        spineAnimation.getColor().a = 0;
//        spineAnimation.addAction(Actions.sequence(
//                Actions.alpha(1, 0.1F),
//                Actions.delay(0.5F),
//                Actions.run(new Runnable() {
//                    @Override
//                    public void run() {
//                        AudioProcess.playSound(AudioType.viewA);
//                        spineAnimation1.setAnimation("animation", true);
//                        SpineActor a = new SpineActor("spine/lizi");
//                        huaKuang.addActor(a);
//                        a.setAnimation("1",true);
//                        float scaleX = spineAnimation1.getScaleX();
//                        spineAnimation1.addAction(Actions.scaleTo(1.04f * scaleX,1.04f*scaleX,3.5f));
//                    }
//                })));
//
//        spineAnimation1.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
//            @Override
//            public void complete(AnimationState.TrackEntry entry) {
//                spineAnimation1.getAnimaState().clearListeners();
//                super.complete(entry);
//                if (ArtPuzzlePreferece.getInstance().getCurrentLevel() == 3) {
//                    if (Gdx.app.getVersion() > 21) {
//                        GameStaticInstance.gameListener.newRate();
//                    } else {
//                        //弹窗
//                        GameStaticInstance.gameListener.rate();
//                    }
//                }
//                successAnimation();
//                EffectTool leftCaidailizi = new EffectTool("lizi/1");
//                huaKuang.addActor(leftCaidailizi);
//                leftCaidailizi.setX(-Constant.GAMEWIDTH/2 - 100);
//                leftCaidailizi.setY(-250);
//
//                EffectTool youCaidailizi = new EffectTool("lizi/2");
//                huaKuang.addActor(youCaidailizi);
//                youCaidailizi.setX(Constant.GAMEWIDTH/2+ 100);
//                youCaidailizi.setY(-250);
//            }
//        });
//        successView();
//        grayBgImage(append, assetManager);
//        return true;
//    }

//    private void grayBgImage(String append, AssetManager assetManager) {
//        SpineActor spineAnimation1 = new SpineActor(append + "/finish", append + "/levelAtlas.atlas", assetManager);
//        addActor(spineAnimation1);
//        spineAnimation1.setPosition(680.0f/2,385);  //916
//        BufferBufferUtils bufferBufferUtils = new BufferBufferUtils(spineAnimation1);
//        addActor(bufferBufferUtils);
////                        "width":680,"height":916,
//        TextureRegion region = bufferBufferUtils.getBufferTexture(1.0f);
//        GrayImage3 grayImage3 = new GrayImage3(region);
//        grayImage3.setOrigin(Align.top);
//        grayImage3.setScale(Math.max(Constant.GAMEWIDTH/680.0f,Constant.GAMEHIGHT/916.0f));
//        Vector2 top = new Vector2(0,Constant.GAMEHIGHT);
//        this.stageToLocalCoordinates(top);
//        grayImage3.setPosition(Constant.GAMEWIDTH/2,top.y ,Align.top);
//        addActor(grayImage3);
//        grayImage3.toBack();
//        grayImage3.addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(0.3667f,new BseInterpolation(
//                0,0,0.75f,1
//        ))));
//        Group game_bottom = rootView.findActor("game_bottom");
//        game_bottom.toFront();
//    }
    Image finishBg;


    public void successDisPlay(){
        Actor img_border = rootView.findActor("img_border");
        img_border.setVisible(false);
        finishBg = new Image(new NinePatch(
                Asset.getAsset().getTexture("cocos/finish/img_frame.png"),
                63,63,63,76
        ));

        huaKuang.addActor(finishBg);

//        finishBg.toFront();
        finishBg.setSize(750, 980);
        finishBg.setPosition(
                spineAnimation.getX(Align.center) - 1,
                spineAnimation.getY(Align.center)-6,
                Align.center);
        finishBg.setOrigin(Align.center);

        huaKuang.getColor().a = 0;
        huaKuang.setScale(0.7F);
        huaKuang.addAction(Actions.parallel(
                Actions.sequence(
                        Actions.fadeIn(0.333f)
                ),
                Actions.sequence(
                        Actions.scaleTo(
                                1,
                                1,
                                0.5F,
                                new BseInterpolation(0,0.04f,0.75f,1.0f))
                        )
        ));

        huaKuang.addAction(Actions.sequence(
                Actions.delay(1.58333f-1.0f),Actions.run(()->{

                    bjg = new SpineActor("spine/beijingguang");
                    addActor(bjg);
                    huaKuang.toFront();
                    bjg.setVisible(false);
                    bjg.setScale(0.7f);
                    bjg.setTimeScale(2f);
                    bjg.setPosition(huaKuang.getX(Align.center),huaKuang.getY(Align.center)-50,Align.center);
                    bjg.setAnimation("guang",true);

                })));
    }


    public void successView(){

        finishBg = new Image(new NinePatch(
                Asset.getAsset().getTexture("cocos/finish/img_frame.png"),
                63,63,63,76
        ));

        huaKuang.addActor(finishBg);
//        finishBg.toFront();
        finishBg.setSize(750, 980);
        finishBg.setPosition(
                spineAnimation.getX(Align.center) - 1,
                spineAnimation.getY(Align.center)-6,
                Align.center);
        finishBg.setOrigin(Align.center);
        finishBg.setVisible(false);

        finishBg.addAction(
                Actions.parallel(
//                        Actions.sequence(Actions.alpha(0),Actions.fadeIn(0.1667f)),
                        Actions.sequence(
                                Actions.scaleTo(1+0.292f,
                                        1+0.292f,0),
                                Actions.delay(1.3f - 1.0f),
                                Actions.visible(true),
                                Actions.scaleTo(1,1,0.25f,new BseInterpolation(0.25f,0,1.0f,1.0f)),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                })
                        )
                ));

        if (levelPicGroup!=null) {
            float scaleX = levelPicGroup.getScaleX();
            levelPicGroup.addAction(Actions.sequence(
                    Actions.delay(1.58333f - 1.0f),
                    Actions.parallel(
                            Actions.run(() -> {

                                Actor img_border = rootView.findActor("img_border");
                                img_border.addAction(Actions.fadeOut(0.1f));

                            }),
                            Actions.sequence(
                                    Actions.scaleTo(
                                            0.9467F * scaleX, 0.9467F * scaleX, 0.0633f),
                                    Actions.scaleTo(
                                            scaleX, scaleX, 0.0633f)
                            ))));
        }
        huaKuang.addAction(Actions.sequence(
                Actions.delay(1.58333f-1.0f),
                Actions.scaleTo(0.9467F,0.9467F,0.0633f,
                        new BseInterpolation(0.25f,0,1,1)),
                Actions.scaleTo(1,1,0.1f,new BseInterpolation(
                        0.25f,0,1,1
                )),Actions.run(new Runnable() {
                    @Override
                    public void run() {

                    }
                })));
        huaKuang.addAction(Actions.sequence(
                Actions.delay(1.58333f-1.0f),Actions.run(()->{

                    bjg = new SpineActor("spine/beijingguang");
                    addActor(bjg);
                    huaKuang.toFront();
                    bjg.setVisible(false);
                    bjg.setScale(0.6f);
                    bjg.setTimeScale(2f);
                    bjg.setPosition(huaKuang.getX(Align.center),huaKuang.getY(Align.center)-50,Align.center);
                    bjg.setAnimation("guang",true);

                })));

    }
    SpineActor bjg;

    public void successAnimation(){
        float x = spineAnimation.getX(Align.center);
        float v = Constant.GAMEHIGHT - 1280;
        Vector2 targetPos = new Vector2(x,750.0f+v/2.0f);
        stageToLocalCoordinates(targetPos);
        float scale = 0.93f;
        huaKuang.addAction(Actions.sequence(Actions.scaleTo(1.1f,1.1f,0.3f),
                Actions.parallel(Actions.moveToAligned(getWidth()/2,targetPos.y,Align.center,0.2f),
                        Actions.scaleTo(579f/716.0f * scale,579f/716.0f * scale,0.2f))));

//        light_kuang

        SpineActor actor = new SpineActor("spine/light_kuang");
        Image huangKuang = new Image(Asset.getAsset().getTexture("cocos/finish/light_kuang.png"));
        huaKuang.addActor(huangKuang);
        huaKuang.addActor(actor);
        actor.setPosition(2,-100);
        actor.setTimeScale(1.3f);
        actor.setVisible(false);
//        actor.setPosition();
        huangKuang.setVisible(false);
//        huangKuang.getColor().a = 0;
        huangKuang.setSize(709, 930);
        huangKuang.setPosition(0,0,Align.center);
        huangKuang.setOrigin(Align.center);
        huangKuang.toBack();
        huangKuang.addAction(
                Actions.parallel(
                    Actions.sequence(
                            Actions.delay(0.6f),
                            Actions.fadeIn(0),
                            Actions.fadeOut(0.13333f)
                    ),Actions.parallel(
                            Actions.delay(0,Actions.run(()->{
                                bjg.setVisible(true);
                                bjg.setTimeScale(1.0f * 0.5f);
                                bjg.setAnimation("guang2",false);
                                bjg.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
                                    @Override
                                    public void complete(AnimationState.TrackEntry entry) {
                                        super.complete(entry);
                                        bjg.setAnimation("guang",true);
                                        bjg.setTimeScale(30.0f/35.0f * 0.5f);
                                    }
                                });
                            })),
                            Actions.sequence( Actions.delay(0.6f),
                        Actions.scaleTo(1.378f,1.378f,0.2333f,new BseInterpolation(
                                0,0.01f,0.4555f,1
                        )),Actions.run(()->{
                            SpineActor spineActor  = new SpineActor("spine/h_lizi");
//                            EffectTool effectTool = new EffectTool("lizi/xxx/1");
                            huaKuang.addActor(spineActor);
                            spineActor.toBack();

                            spineActor.setScale(1.2f);
                            spineActor.setY(-50);
                            spineActor.setAnimation("h_lizi",true);

                        })))
                )
        );

        huangKuang.addAction(Actions.delay(0.5f,Actions.run(new Runnable() {
            @Override
            public void run() {
                actor.setVisible(true);
                actor.setAnimation("light_kuang",false);
            }
        })));


//        Image congratu = new Image(Asset.getAsset().getTexture("cocos/finish/line.png"));
//        addActor(congratu);
//        congratu.toBack();
//        congratu.setPosition(huaKuang.getX(Align.center), huaKuang.getY(Align.top),Align.top);
        Group continueButton = CocosResource.loadFile("cocos/FinishButton.json");
        getStage().addActor(continueButton);
        continueButton.setPosition(getWidth()/2,208,Align.bottom);
        continueButton.setTouchable(Touchable.enabled);
        continueButton.setOrigin(Align.center);
        continueButton.getColor().a = 0;
        continueButton.findActor("Home_2").setVisible(false);
        if (LevelConfig.isFinshAll){
            if (LevelConfig.levelNum-1 == ArtPuzzlePreferece.getInstance().getCurrentLevel()){
                continueButton.findActor("Home_2").setVisible(true);
                continueButton.findActor("Continue_1").setVisible(false);
            }
        }
        continueButton.setTouchable(Touchable.disabled);
        continueButton.addAction(Actions.parallel(
                Actions.sequence(
                        Actions.delay(0.433f),
                        Actions.fadeIn(.0367f)
                        ),
                Actions.sequence(
                        Actions.scaleTo(0.499f,0.499f),
                        Actions.delay(0.4333f),
                        Actions.scaleTo(1.05f,1.05f,0.4f,
                                new BseInterpolation(0,0.01f,0.75f,1.0f)),
                        Actions.scaleTo(1.0f,1.0f,0.23367f),
                        Actions.touchable(Touchable.enabled)
                )
        ));
        continueButton.addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                continueButton.setTouchable(Touchable.disabled);
                getaVoid(huaKuang);

                continueButton.setOrigin(Align.center);
                continueButton.addAction(
                        Actions.delay(0.33f,
                                Actions.parallel(
                                        Actions.fadeOut(1.0F)
                                )));
                bjg.addAction(Actions.fadeOut(0.3f));
                addAction(Actions.delay(0.5f,Actions.run(()->{
                    runnable.run();
                })));

                addAction(Actions.delay(2.0f,Actions.run(()->{
                    LevelView.this.dispose();
                    Constant.currentScreen.setScreen(MainScreen.class);
                })));
//                continueButton.addAction(Actions.sequence(
//                        Actions.delay(0.5f),
//                        Actions.scaleTo(0.924f,0.924f,0.2333f,new BseInterpolation(0,0,0.75f,1.f)),
//                        Actions.scaleTo(1.0f,1.0f,0.1667f,new BseInterpolation(0.25f,0,1f,1.f))
//                ));

            }
        });

        huangBg.addAction(Actions.fadeIn(0.8f));
    }

    private void getaVoid(Actor group) {
        group.setOrigin(Align.center);
        group.addAction(
                Actions.delay(0.33f,
                Actions.parallel(
                        Actions.fadeOut(1.0F),
                        Actions.sequence(Actions.scaleTo(0.6f, 0.6F, 0.4F)))));
    }

    public Array<String> getSuccessPic() {
        return successPic;
    }

    public HistoryBean getHistoryData() {
        if (getSuccessPic().size <= 0) return null;
        HistoryBean bean = new HistoryBean();
        bean.setAlreadyPic(getSuccessPic());
        bean.setCengshu(levelCengNum);
        return bean;
    }

    float currentScrollPanelX = 0;
    private Vector2 tarGetPosVec2 = new Vector2();


    private boolean isTipEnable = true;
    public boolean isAble(){
        if (!isTipEnable)return false;
        return true;
    }

    public void tip(){
//        if (isAble()){
//            return;
//        }

        if (!isTipEnable) {
            return;
        }
        isTipEnable = false;
        Array<String> array = everyCengResourceMap.get(levelCengNum);
        if (array==null)return;
        String s = array.get(0);
        for (Actor child : levelPicLayout.getChildren()) {
            if (child == null||child.getName() == null) {
                continue;
            }
            ((ImageActor)(child)).setTarget(1);
            if (child.getName().equals(s)) {
                Actor s1 = levelPicGroup.findActor(child.getName());
                if (tipAction!=null) {
                    if (tipAction.getActor()!=null) {
                        if (handImage!=null) {
                            handImage.addAction(Actions.fadeOut(0.2f));
                            nextGuideAble = true;
                        }
                        tipAction.getActor().removeAction(tipAction);
                    }
                    tipAction = null;
                }
                addAction(moveAction1(s1,child));
//                addAction(moveAction(s1,child));
                break;
            }
        }
    }

    private float delay2 ;
    private float delay3 ;
    private boolean currentTip = true;
    float xxxx =0;
    float delay = 0;
    private Action moveAction1(final Actor s1,final Actor child){
        bottomScrollPane.addAction(new Action() {
            @Override
            public boolean act(float v) {
                if (gameTouchDown){
                    if (handImage!=null){
                        handImage.addAction(Actions.fadeOut(0.3f));
                        nextGuideAble = true;
                    }
                    currentTip = false;
                }
                return false;
            }
        });

        return tipAction = Actions.delay(0f,
                Actions.forever(
                        Actions.parallel(
                                Actions.sequence(
                                        new Action() {
                                            @Override
                                            public boolean act(float v) {
                                                if (!nextGuideAble) return false;
                                                if (gameTouchDown) {
                                                    xxxx = 0;
                                                }
                                                if (bottomScrollPane.getFlingTimer() > 0) {
                                                    xxxx = 0;
                                                }
                                                xxxx += v;
                                                if (xxxx > delay) {
                                                    return true;
                                                }
                                                return false;
                                            }
                                        },
                                        new Action() {
                                            @Override
                                            public boolean act(float v) {
                                                if (currentTip) {
                                                    currentScrollPanelX = child.getX()-Constant.GAMEWIDTH/2-110;
                                                    bottomScrollPane.setScrollX(currentScrollPanelX);
//                                                    bottomScrollPane.updateVisualScroll();
                                                }
                                                return true;
                                            }
                                        },
                                        new Action() {
                                            @Override
                                            public boolean act(float v) {
                                                if (!currentTip){
                                                    delay2 = 0;
                                                    return true;
                                                }
                                                delay2+=v;
                                                if (delay2>=1){
                                                    delay2 = 0;
                                                    return true;
                                                }
                                                return false;
                                            }
                                        },
        new Action() {
                                            @Override
                                            public boolean act(float delta) {
                                                if (!currentTip){
                                                    currentTip = true;
                                                    xxxx = 0;
                                                    delay = 1;
                                                    return true;
                                                }
                                                if (!nextGuideAble) return false;
                                                nextGuideAble = false;
                                                Vector2 tarV2 = new Vector2();
                                                tarV2.set(s1.getX(Align.center), s1.getY(Align.center));
                                                levelPicGroup.localToStageCoordinates(tarV2);

                                                ImageActor child3 = (ImageActor) (child);
                                                Actor child1 = child3.findActor("trueImage");
                                                Vector2 srcV2 = new Vector2();
                                                srcV2.set(child1.getX(Align.center),
                                                        child1.getY(Align.center));
                                                if (child.getParent() == null) {
                                                    nextGuideAble = true;
                                                    return true;
                                                }
                                                child1.getParent().localToStageCoordinates(srcV2);
                                                handImage = new SpineActor("spine/szdj");
                                                getStage().addActor(handImage);
                                                handImage.setAnimation("dianji11", true);
                                                handImage.getColor().a = 0;
                                                handImage.setOrigin(Align.center);
                                                handImage.setTouchable(Touchable.disabled);
                                                handImage.setPosition(srcV2.x + 120, srcV2.y - 130, Align.center);
                                                float v = srcV2.x;
                                                float v1 = srcV2.y;
                                                float v3 = tarV2.x;
                                                float v4 = tarV2.y;
                                                float v5 = (float) Math.sqrt((v3 - v) * (v3 - v) + (v4 - v1) * (v4 - v1));
                                                float moveTime = Math.abs(v5 / 663.0f);
                                                handImage.addAction(
                                                        Actions.parallel(
                                                                Actions.sequence(
                                                                        Actions.fadeIn(0.333f,
                                                                                new BseInterpolation(0, 0, 0.75f, 1.0f)),

                                                                        Actions.run(() -> {
                                                                            handImage.setAnimation("dianji1", false);
                                                                            handImage.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
                                                                                @Override
                                                                                public void complete(AnimationState.TrackEntry entry) {
                                                                                    super.complete(entry);
                                                                                    handImage.getAnimaState().clearListeners();
                                                                                    handImage.setAnimation("dianji2", true);
                                                                                }
                                                                            });
                                                                        })
                                                                )
                                                        )
                                                );

                                                handImage.addAction(
                                                        Actions.sequence(
                                                                Actions.delay(0.8f),
                                                                Actions.moveToAligned(
                                                                        tarV2.x + 120,
                                                                        tarV2.y - 130,
                                                                        Align.center,
                                                                        moveTime,
                                                                        new BseInterpolation(0, 0, 0.573f, 0.99f)),
                                                                Actions.run(() -> {
                                                                    handImage.setAnimation("dianji3", false);
                                                                    handImage.getAnimaState().clearListeners();
                                                                    handImage.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
                                                                        @Override
                                                                        public void complete(AnimationState.TrackEntry entry) {
                                                                            super.complete(entry);
                                                                            handImage.addAction(
                                                                                    Actions.sequence(Actions.fadeOut(0.5f),
                                                                                            Actions.delay(0.6f),
                                                                                            Actions.run(() -> {
                                                                                                nextGuideAble = true;
                                                                                            }),
                                                                                            Actions.removeActor()));
                                                                        }
                                                                    });
                                                                })
                                                        ));
                                                return true;
                                            }
                                        }
                                )

                        )));
    }

    public SpineActor getHandImage() {
        return handImage;
    }

    boolean nextGuideAble = true;
    private Action tipAction;
    private SpineActor handImage;
    private Action moveAction(final Actor s1,final Actor child){
        return tipAction = Actions.delay(0f,
                Actions.forever(
                Actions.sequence(

        new Action() {
                            @Override
                            public boolean act(float delta) {
                                if (!nextGuideAble)return false;
                                nextGuideAble = false;
                                Vector2 tarV2 =  new Vector2();
                                tarV2.set(s1.getX(Align.center),s1.getY(Align.center));
                                levelPicGroup.localToStageCoordinates(tarV2);

                                ImageActor child3 = (ImageActor) (child);
                                Actor child1 = child3.findActor("trueImage");
                                Vector2 srcV2 = new Vector2();
                                srcV2.set(child1.getX(Align.center) ,
                                        child1.getY(Align.center));
                                if (child.getParent() == null) {
                                    nextGuideAble = true;
                                    return  true;
                                }
                                child1.getParent().localToStageCoordinates(srcV2);
                                handImage = new SpineActor("spine/szdj");
                                getStage().addActor(handImage);
                                handImage.setAnimation("dianji11",true);
                                handImage.getColor().a = 0;
                                handImage.setOrigin(Align.center);
                                handImage.setTouchable(Touchable.disabled);
                                handImage.setPosition(srcV2.x+120,srcV2.y-130,Align.center);
                                float v = srcV2.x;
                                float v1 = srcV2.y;
                                float v3 = tarV2.x;
                                float v4 = tarV2.y;
                                float v5 = (float) Math.sqrt((v3 - v) * (v3 - v) + (v4 - v1) * (v4 - v1));
                                float moveTime = Math.abs(v5 / 663.0f);
                                handImage.addAction(
                                        Actions.parallel(
                                                Actions.sequence(
                                                        Actions.fadeIn(0.333f,
                                                                new BseInterpolation(0,0,0.75f,1.0f)),

                                                            Actions.run(()->{
                                                                handImage.setAnimation("dianji1",false);
                                                                handImage.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
                                                                    @Override
                                                                    public void complete(AnimationState.TrackEntry entry) {
                                                                        super.complete(entry);
                                                                        handImage.getAnimaState().clearListeners();
                                                                        handImage.setAnimation("dianji2",true);
                                                                    }
                                                                });
                                                            })
                                                )
                                        )
                                );

                                handImage.addAction(
                                        Actions.sequence(
                                                Actions.delay(0.8f),
                                                Actions.moveToAligned(
                                                        tarV2.x + 120,
                                                        tarV2.y - 130,
                                                        Align.center,
                                                        moveTime,
                                                        new BseInterpolation(0,0,0.573f,0.99f)),
                                                Actions.run(()->{
                                                    handImage.setAnimation("dianji3",false);
                                                    handImage.getAnimaState().clearListeners();
                                                    handImage.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
                                                       @Override
                                                        public void complete(AnimationState.TrackEntry entry) {
                                                            super.complete(entry);
                                                            handImage.addAction(
                                                                    Actions.sequence(Actions.fadeOut(0.5f),
                                                                            Actions.delay(0.6f),
                                                                            Actions.run(()->{
                                                                                nextGuideAble = true;
                                                                            }),
                                                                            Actions.removeActor()));
                                                        }
                                                    });
                                                })
                                        ));
                                return true;
                            }
                        })
        ));
    }

    public int getLevelCengNum() {
        return levelCengNum;
    }

    public boolean pass = false;
    public void nextCeng(SignListener signListener) {
        if (pass)return;
        if (everyCengResourceMap.size<=0){
            pass = true;
            Group ad = rootView.findActor("ad");
            ad.setTouchable(Touchable.disabled);
            success();
            return;
        }
        Array<String> everyCengResourceArray = getEveryCengResourceArray(this.everyCengResourceMap, levelCengNum);
        for (String s : everyCengResourceArray) {
            levelPicGroup.findActor(s).setVisible(true);
            if (levelPicLayout.findActor(s)!=null) {
                ImageActor actor = levelPicLayout.findActor(s);
                Array<String> afterShow = actor.getAfterShow();
                if (afterShow !=null) {
                    for (String s1 : afterShow) {
//                        .setVisible(true);
                        Actor actor1 = levelPicGroup.findActor(s1);
                        if (actor1 == null) {
                            NLog.e("actor1 is null!");
                        }else {
                            actor1.setVisible(true);
                        }
                    }
                }
                currentCount ++;
                levelPicLayout.findActor(s).remove();
            }
        }
        update();

        levelPicLayout.peak();
        everyCengResourceMap.removeKey(levelCengNum);
        String s = tipLineMap.get(levelCengNum-1);
        if (s!=null) {
            Actor actor = levelPicGroup.findActor(s);
            if (actor != null) {
                actor.setVisible(false);
            }
        }
        levelCengNum++;
        signListener.sign(levelCengNum);
        String s1 = tipLineMap.get(levelCengNum-1);
        Actor actor1 = levelPicGroup.findActor(s1);
        actor1.setVisible(true);
    }

    public HistoryNum getPercent() {
        HistoryNum num = new HistoryNum();
        num.setAllCount(allCount);
        num.setCurrentCount(currentCount);
        return num;
    }

    public Actor getLevelPicGroup() {
        return levelPicGroup;
    }

    public void dispose(){
        //
        if (levelPicGroup!=null) {
            levelPicGroup.dispose();
        }
        String version = InitCsvData.orderHashMap.get(currentLevel).getVersion();
        if (currentLevel == 1){
            UnloadingFile.unloadResource(version + "/" + LevelConfig.levelDirPath + LevelConfig.levelNumIndex + "/" + "levelAtlas.atlas");
        }else {

        }
        String append = StringUtils.append(version + "/level/level"+LevelConfig.levelNumIndex+"/finish.json");
        UnloadingFile.unloadResource(append);
    }



    public void setSpineAnimationVisible() {
        if (spineAnimation1==null)return;
        spineAnimation1.setVisible(true);
        spineAnimation1.getColor().a = 0;
        spineAnimation1.addAction(Actions.fadeIn(0.2f));
        if (LevelConfig.gameStatus == LevelConfig.ENTERGAME) {

            Actor img_border = rootView.findActor("img_border");
            spineAnimation.addActor(img_border);
            img_border.toFront();
            img_border.setVisible(true);
            img_border.setPosition(spineAnimation1.getX(Align.center), spineAnimation1.getY(Align.center),Align.center);
            img_border.setScale(1.0f/LevelConfig.globalScale+0.01f);
            img_border.addAction(Actions.delay(0.5f,Actions.fadeOut(0.2f)));

        }
    }

    private boolean isLoading = false;
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        LevelConfig.gameCurrentLevelPlayTime += delta;
    }
}
