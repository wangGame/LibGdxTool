package kw.artpuzzle.group;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.framebuffer.BufferBufferUtils;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.utils.log.StringUtils;
import com.kw.gdx.zip.PackZip;

import kw.artpuzzle.asset.SpineRes;
import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.csv.InitCsvData;
import kw.artpuzzle.history.LevelHistoryUtils;
import kw.artpuzzle.history.bean.HistoryNum;
import kw.artpuzzle.listener.LevelItemListener;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.artpuzzle.shadermanager.ShaderManager;
import kw.artpuzzle.shadermanager.ShaderType;
import kw.artpuzzle.theme.GameTheme;

public class LevelItemGroup extends Group {
    private int levelNum;
    private boolean imageValid; //no avil ,need download
    private ItemListener itemListener;
    private int status = LevelConfig.NOLOCK;
    private ShaderType type;
    private Group cocosGroup;
    private SpineActor loadingSpine;
    private int index;
    private String version;

    public int getLevelNum() {
        return levelNum;
    }

    public LevelItemGroup(int levlNum, int levelScrollPanelIndex,ItemListener listener){
        setName("levelItem"+levlNum);
        this.itemListener = listener;
        this.index = levelScrollPanelIndex;
//        this.index = levlNum;
        this.version = InitCsvData.preOrderHashMap.get(levlNum).getVersion();

        this.levelNum = levlNum;
        cocosGroup = CocosResource.loadFile("cocos/mainBottomItem.json");
        setSize(cocosGroup.getWidth(),cocosGroup.getHeight());
        addActor(cocosGroup);
        setOrigin(Align.center);
        FileHandle local;

        if (LevelConfig.useInocal) {
            local = Gdx.files.internal(version + "/levelpre/level" + index);
        }else {
            local = Gdx.files.local(version + "/levelpre/level" + index);
        }
        //特殊处理
        if (levelNum == 1){
            local = Gdx.files.internal(version + "/levelpre/level" + index);
        }
        setLevelMsg();
        if (status!=LevelConfig.NOLOCK) {

            LevelItemListener levelItemListener = new LevelItemListener(0.946f) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        LevelConfig.LOCKANIMATION = false;
                        listener.openLevel(index,levelNum, status);
                }
            };
            levelItemListener.setTimeScale(1.0f);
            addListener(levelItemListener);
        }
        if (!local.exists()) {

//            findActor("Image_1").setVisible(false);
            NLog.e("file not found!!!! %s",levlNum);
            loadingSpineAnimation();
            return;
        }
        boolean check = false;

        try {
            if (LevelConfig.useInocal){
                check = PackZip.check(local);
            }else {
                check = PackZip.check(local);
            }
        }catch (Exception e){
            if (levelNum !=1) {
                local.deleteDirectory();
            }
            check=false;
        }

        if (!check){
            if (levelNum !=1) {
                local.deleteDirectory();
            }
            loadingSpineAnimation();
            return;
        }
//        check
        if (local.exists()) {
            imageValid = true;
            showImage();
        } else {
            imageValid = false;
            loadingSpineAnimation();
        }
    }

    private void loadingSpineAnimation() {
        findActor("empty_image").setVisible(false);
        findActor("item_zhe").setVisible(false);
        findActor("item_info").setVisible(false);
        loadingSpine = new SpineActor("spine/jiazai");
        Group panel = cocosGroup.findActor("panel");
        panel.addActor(loadingSpine);
        loadingSpine.setPosition(getWidth()/2,getHeight()/2,Align.center);
        loadingSpine.setAnimation("1",true);
        loadingSpine.setScale(12.0f);


    }

    private boolean needBorderAnimation;
    public void setLevelMsg(){
        findActor("item_shadow").getColor().a  = 1.0f;
        if (LevelConfig.LOCKANIMATION && levelNum == ArtPuzzlePreferece.getInstance().getCurrentLevel() - 1){
            // have
            needBorderAnimation = true;
        }
        if (levelNum< ArtPuzzlePreferece.getInstance().getCurrentLevel()){
            status = LevelConfig.PLAYED;
        }else if (levelNum == ArtPuzzlePreferece.getInstance().getCurrentLevel()){
            if (LevelConfig.isFinshAll) {
                status = LevelConfig.PLAYED;
            }else {
                status = LevelConfig.PLAYING;
            }
        }
        findActor("item_bg").setColor(Color.valueOf("#FBE9C4"));
        findActor("item_shadow").setVisible(false);
        findActor("item_zhe").setVisible(false);
        Group item_info = findActor("item_info");
        item_info.findActor("item_msg_0").setVisible(false);
        item_info.findActor("item_bg_0").setVisible(false);
        Actor item_num_bg = findActor("item_num_bg");
        Label item_process = findActor("item_process");
        item_num_bg.setVisible(false);
        item_process.setVisible(false);
        if (status == LevelConfig.PLAYED){
            findActor("item_info").setVisible(true);
            Label item_msg = item_info.findActor("item_msg");
            item_msg.setText(levelNum);
            item_msg.setColor(Color.BLACK);

//            Label item_msg = item_info.findActor("item_msg");
//            item_msg.setText(levelNum);
//            setTheme(item_info, "item_bg", GameTheme.mainItemPlayedBgColor);
//            setTheme(item_info, "item_lock",Color.valueOf("#FFFBE6"));
//            item_info.findActor("play_inco").setVisible(false);
//            item_msg.setColor(Color.valueOf("#FFFBE6"));
//            findActor("item_kuang").setColor(Color.valueOf("#c7bbaf"));

            findActor("empty_image").setVisible(false);
            findActor("item_zhe").setVisible(false);
            findActor("item_kuang").setColor(Color.WHITE);
            findActor("item_shadow").setVisible(true);
            HistoryNum num = LevelHistoryUtils.readPercent(index);
            if (num!=null) {
                int perent = num.getPerent();
                if (perent>0&&perent<100) {
                    item_num_bg.setVisible(true);
                    item_process.setVisible(true);
                    item_process.setText(perent + "%");
                }
            }
        }else if (status == LevelConfig.PLAYING){
            if (LevelConfig.LOCKANIMATION) {
                item_info.findActor("item_msg_0").setVisible(true);
                item_info.findActor("item_bg_0").setVisible(true);
                item_info.findActor("item_bg").setScale(0.01f);
                item_info.findActor("item_msg").setScale(0.01f);
                item_info.findActor("play_inco").setScale(0.01f);

//                item_info.findActor("item_msg").setVisible(false);
                Label item_msg_0 = item_info.findActor("item_msg_0");
                item_msg_0.setText(levelNum);
                item_msg_0.setColor(Color.WHITE);
                item_info.findActor("item_bg_0")
                        .setColor(GameTheme.mainItemPlayedBgColor);
            }else {
                Label item_msg = item_info.findActor("item_msg");
//                item_msg.setVisible(false);
                findActor("item_shadow").setVisible(true);
                item_info.findActor("item_lock").setVisible(false);
                item_msg.setColor(GameTheme.mainItemPlayingInfoColor);
                item_info.findActor("item_bg").setColor(Color.WHITE);
                findActor("item_kuang").setColor(Color.WHITE);
            }
            findActor("item_kuang").setColor(Color.valueOf("#d6c5a3"));
            HistoryNum num = LevelHistoryUtils.readPercent(index);
            if (num!=null) {
                int perent = num.getPerent();
                if (perent>0&&perent<100) {
                    item_num_bg.setVisible(true);
                    item_process.setVisible(true);
                    item_process.setText(perent + "%");
                }
            }


        }else if (status == LevelConfig.NOLOCK){
            type = ShaderType.Hui;
            Label item_msg = item_info.findActor("item_msg");
            item_msg.setText(levelNum);
            setTheme(item_info, "item_bg", Color.valueOf("604f3e"));
            item_info.findActor("item_bg").getColor().a =  0.8f;
            setTheme(item_info, "item_lock",Color.valueOf("#FFFBE6"));
            item_info.findActor("play_inco").setVisible(false);
            item_msg.setColor(Color.valueOf("#FFFBE6"));
            findActor("item_kuang").setColor(Color.valueOf("#d6c5a3"));
        }



        Actor item_lock = item_info.findActor("item_lock");
        item_lock.setX(100.00f,Align.center);
        Actor item_msg = item_info.findActor("item_msg");
        item_msg.setX(145.00f,Align.center);
        if (levelNum>=10&&levelNum<=99){
            item_msg.setX(item_msg.getX()+5);
            item_lock.setX(item_lock.getX()-5);
        }else if (levelNum>=100){
            item_msg.setX(item_msg.getX()+10);
            item_lock.setX(item_lock.getX()-10);
        }
//
        if (status == LevelConfig.PLAYING&& LevelConfig.LOCKANIMATION) {
            isAnimation = true;
        }
    }

    private boolean isAnimation = false;

    public Group borderAnimation(){
        if (!imageValid)return null;
//        if (!isAnimation)return;

        if (!needBorderAnimation)return null;
//        Gdx.app.postRunnable(new Runnable() {
//            @Override
//            public void run() {
////                Actor item_kuang = cocosGroup.findActor("item_kuang");
////                item_kuang.addAction(Actions.scaleTo(2,2));
//                cocosGroup.toFront();
//                cocosGroup.setOrigin(Align.center);
//                cocosGroup.setScale(2);
//            }
//        });

        String loadFilePath = StringUtils.append(version + "/levelpre/level",index,"/","pre.png");
        Texture texture;
        if (LevelConfig.useInocal){
            texture = Asset.getAsset().getTexture(loadFilePath);
        }else {
            texture = Asset.getAsset().getLocalTexture(loadFilePath);
        }
        if (levelNum == 1){
            texture = Asset.getAsset().getTexture(loadFilePath);
        }

        Image item_kuang = cocosGroup.findActor("item_kuang");
        NinePatch patch = ((NinePatchDrawable) (item_kuang.getDrawable())).getPatch();
        Group group = new Group();
        group.setSize(item_kuang.getWidth(),item_kuang.getHeight());
        Image image = new Image(patch);
        image.setSize(324.00F,324.00f);
        group.addActor(image);
        image.setPosition(group.getWidth()/2,group.getHeight()/2,Align.center);
        Image pic = new Image(texture);
        group.addActor(pic);
        pic.setSize(310,310);
        pic.toBack();
        pic.setPosition(group.getWidth()/2,group.getHeight()/2,Align.center);
        group.setSize(324f,324f);

        return group;
    }

    public void playingPic(){

        Group group1 = CocosResource.loadFile("cocos/mainBottomItem.json");
//            Image image = new Image(new Texture("level4.png"));
//            image.setSize(234,234);
        Actor item_info1 = group1.findActor("item_info");
        item_info1.setVisible(false);
        Actor empty_image = group1.findActor("empty_image");
        empty_image.setVisible(false);
        Actor item_num_bg1 = group1.findActor("item_num_bg");
        item_num_bg1.setVisible(false);
        Actor item_process1 = group1.findActor("item_process");
        item_process1.setVisible(false);

        String loadFilePath = StringUtils.append(version+"/levelpre/level",index,"/","pre.png");
        Texture texture;
        if (LevelConfig.useInocal){
            texture = Asset.getAsset().getTexture(loadFilePath);
        }else {
            texture = Asset.getAsset().getLocalTexture(loadFilePath);
        }
        if (levelNum == 1){
            texture = Asset.getAsset().getTexture(loadFilePath);
        }

        Image picImage = new Image(texture){

            @Override
            public void act(float delta) {
                super.act(delta);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                ShaderProgram type = ShaderManager.getManager().getType(ShaderType.Hui2);
                batch.setShader(type);
                type.setUniformf("contrast",0.65f);
                super.draw(batch, parentAlpha);
                batch.flush();
                batch.setShader(null);
            }
        };
//        Interpolation.fade
        Color color = Color.valueOf("#dcb594");
        picImage.setColor(color);
        group1.findActor("item_bg").setColor(Color.valueOf("dbc59a"));
        group1.findActor("item_bg").getColor().a = 0.2f;

        group1.findActor("empty_image").setVisible(false);
        Group panel = group1.findActor("panel");
        panel.addActor(picImage);
        picImage.toBack();
        picImage.setSize(310,310);
        picImage.setPosition(getWidth()/2,getHeight()/2,Align.center);
        group1.findActor("item_bg").toBack();

        Image shadow = new Image(Asset.getAsset().getTexture("common/white_bg.png"));
        shadow.setSize(group1.getWidth(),group1.getHeight());
        shadow.getColor().a = 1.0f;
        shadow.addAction(Actions.fadeOut(0.66f,new BseInterpolation(
                0.25f,
                0.0f,
                1.0f,
                0.99f
        )));
//        group1.addActor(shadow);
        group1.findActor("item_zhe").setVisible(false);
        cocosGroup.findActor("item_shadow").setVisible(false);
        addAction(Actions.delay(0.9f,Actions.run(()->{
            Actor item_shadow = cocosGroup.findActor("item_shadow");
            item_shadow.setVisible(true);
            item_shadow.getColor().a = 0;
            item_shadow.addAction(Actions.fadeIn(0.4f));
        })));
        BufferBufferUtils utils = new BufferBufferUtils(group1);
//        utils.setDebug(true);
        TextureRegion colorBufferTexture = utils.getBufferTexture(1.0f);
        utils.setTouchable(Touchable.disabled);
        Constant.currentScreen.addActor(utils);
        CirGroup group = new CirGroup(colorBufferTexture);
        group.setDelay(0.4f);
        cocosGroup.addActor(group);

//        addAction(Actions.delay(1,Actions.run(()->{
//            cocosGroup.findActor("panel").setVisible(false);
//        })));
////
        SpineActor spineActor = new SpineActor("spine/paly");
        Group item_info = cocosGroup.findActor("item_info");
        addActor(spineActor);
        Actor play_inco = item_info.findActor("play_inco");
        spineActor.setPosition(play_inco.getParent().getX(Align.center),
                play_inco.getParent().getY(Align.center),Align.center);
        spineActor.setVisible(false);
        addAction(Actions.delay(1.0f,Actions.run(()->{
            spineActor.setVisible(true);
            spineActor.setAnimation("animation",false);
        })));
        item_info.toFront();
    }

    private void setTheme(Group item_info, String item_bg, Color mainItemPlayedBgColor) {
        item_info.findActor(item_bg).setColor(mainItemPlayedBgColor);
    }

    public void showImage() {
        try {
            {
                FileHandle local = Gdx.files.local(version + "/downloadlevelpre/md5/level" + index);
                if (local.exists()) {
                    local.deleteDirectory();
                }
            }
            {
                FileHandle local = Gdx.files.local(version + "/downloadlevelpre/md5/level" + index);
                if (local.exists()) {
                    local.deleteDirectory();
                }
            }
        }catch (Exception e){
            NLog.e("delete error ");
        }
        if (levelNum< ArtPuzzlePreferece.getInstance().getCurrentLevel()){
            type = ShaderType.nomal;

        }else if (levelNum == ArtPuzzlePreferece.getInstance().getCurrentLevel()){
            if (LevelConfig.isFinshAll) {
                type = ShaderType.nomal;
            }else {
                type = ShaderType.Hui3;
            }
        }
        String loadFilePath = StringUtils.append(version + "/levelpre/level",index,"/","pre.png");

        if (levelNum< ArtPuzzlePreferece.getInstance().getCurrentLevel()) {
            {
                Texture textureTemp = null;
                String loadFile = StringUtils.append(version + "/levelpre/level", index, "/", "pre_success.png");
                if (LevelConfig.useInocal) {
                    textureTemp = Asset.getAsset().getTexture(loadFile);
                } else {
                    textureTemp = Asset.getAsset().getLocalTexture(loadFile);
                }
                if (levelNum == 1) {
                    textureTemp = Asset.getAsset().getTexture(loadFile);
                }
                if (textureTemp != null) {
                    loadFilePath = loadFile;
                }
            }
        }
        Texture texture;
        if (LevelConfig.useInocal){
            texture = Asset.getAsset().getTexture(loadFilePath);
        }else {
            texture = Asset.getAsset().getLocalTexture(loadFilePath);
        }
        if (levelNum == 1){
            texture = Asset.getAsset().getTexture(loadFilePath);
        }
        Image picImage = new Image(texture){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                batch.setShader(ShaderManager.getManager().getType(type));
                super.draw(batch, parentAlpha);
                batch.flush();
                batch.setShader(null);
            }
        };
        if (type == ShaderType.Hui){
            Color color = Color.valueOf("#dcb594");
            picImage.setColor(color);
        }
        cocosGroup.findActor("empty_image").setVisible(false);
        Group panel = cocosGroup.findActor("panel");
        panel.addActor(picImage);
        picImage.toBack();
        picImage.setSize(310,310);
        picImage.setPosition(getWidth()/2,getHeight()/2,Align.center);
        cocosGroup.findActor("item_bg").toBack();
        if (loadingSpine!=null) {
            loadingSpine.remove();
            findActor("item_zhe").setVisible(false);
            findActor("item_info").setVisible(true);
        }
        if (status != LevelConfig.PLAYING) {
            findActor("item_bg").setColor(Color.valueOf("dbc28f"));
            findActor("item_bg").getColor().a = 1.0f;
        }else {
            Color color = Color.valueOf("#dcb594");
            picImage.setColor(color);
            findActor("item_bg").setColor(Color.valueOf("dbc59a"));
            findActor("item_bg").getColor().a =1f;
            findActor("item_bg").setVisible(false
            );

        }
        if (status == LevelConfig.PLAYING){
            if (isAnimation){
                Group panel1 = cocosGroup.findActor("panel");
                for (Actor child : panel1.getChildren()) {
                    child.setVisible(false);
                }
                panel1.findActor("item_shadow").setVisible(true);
                playingPic();
            }else {
                Group item_info = findActor("item_info");
                item_info.findActor("item_msg").setVisible(false);
                findActor("item_kuang").setColor(Color.WHITE);
                findActor("item_zhe").setVisible(false);
            }
        }

        if (status == LevelConfig.PLAYED){
            cocosGroup.findActor("item_info").setVisible(false);
        }
    }


    public String getVersion() {
        return version;
    }

    private Group haveAnimationItem;

    public void setAnimation(Stage stage) {

        if (borderAnimation() != null) {
            setVisible(false);
            haveAnimationItem = borderAnimation();
        }

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if(haveAnimationItem!=null){
                    stage.addActor(haveAnimationItem);
                    haveAnimationItem.getColor().a = 0;
                    haveAnimationItem.setOrigin(Align.center);
                    haveAnimationItem.setScale(1.1f);
                    haveAnimationItem.toFront();
                    float x = LevelItemGroup.this.getX(Align.center);
                    float y =  LevelItemGroup.this.getY(Align.center);

                    Vector2 temV2 = new Vector2(x,y);
                    LevelItemGroup.this.getParent().localToStageCoordinates(temV2);
                    if ( LevelItemGroup.this.getLevelNum()%2 == 1) {
                        haveAnimationItem.setPosition(temV2.x+25, temV2.y + 25, Align.center);
                    }else {
                        haveAnimationItem.setPosition(temV2.x - 25, temV2.y + 25, Align.center);
                    }
                    haveAnimationItem.addAction(
                            Actions.parallel(
                                    Actions.fadeIn(0.3333f),
                                    Actions.scaleTo(1.0f,1.0f,0.6667f * 0.8f,new BseInterpolation(
                                            0,0,0.75f,1.0f
                                    )),
                                    Actions.sequence(
                                            Actions.moveToAligned(temV2.x,temV2.y,Align.center, 0.8f * 0.5f,
                                                    new BseInterpolation(
                                                            0.0f,0.f,0.5111f,1.0f
                                                    )),
                                            Actions.run(()->{
                                                LevelItemGroup.this.setVisible(true);
                                                LevelItemGroup.this.findActor("item_shadow").setVisible(true);
                                                LevelItemGroup.this.findActor("item_shadow").addAction(Actions.fadeIn(0.26666f));
                                            }),
                                            Actions.removeActor()
                                    )
                            )
                    );

                }


            }
        });
    }

    public static interface ItemListener{
        void openLevel(int levelId,int levelScrollPanelIndex,int status);
        void addTask(int levelId,LevelItemGroup group);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!imageValid) {
            imageValid = true;
            itemListener.addTask(index, this);
        }
    }
}
