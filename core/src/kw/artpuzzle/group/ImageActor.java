package kw.artpuzzle.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.shadermanager.ShaderManager;
import kw.artpuzzle.shadermanager.ShaderType;

public class ImageActor extends Group {
    private Group hintGroup;
    private float baseWidth;
    private float baseHight;
    private float baseImageX;
    private float baseImageY;
    private Image levelImage;
    private boolean isAnimation;
    private float touchWidth;
    private float touchHight;
    private float weiSuiTime = 0.2F;
    private float huifuTime = 0.3F;
    private float maxScale;
    private Array<String> afterShow;
    protected ShaderProgram shaderProgram;
    private boolean isAnimationStart;

    public ImageActor(SpriteDrawable image, String index){
        levelImage = new Image(image){
            private float timeTotal = 0;

            @Override
            public void act(float delta) {
                super.act(delta);
                if (isAnimationStart) {
                    setPosition(hintGroup.getWidth() / 2, hintGroup.getHeight() / 2, Align.center);
                }
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (shaderProgram!=null) {
                    batch.setShader(shaderProgram);
                    int time1 = shaderProgram.getUniformLocation("ccc");
                    if (timeTotal * 1.8f > 0.7f){
                        shaderProgram.setUniformf(time1, 0.7f);
                    }else {
                        shaderProgram.setUniformf(time1, timeTotal * 1.8F);
                    }
                    super.draw(batch, parentAlpha);
                    timeTotal += Gdx.graphics.getDeltaTime();
                    batch.setShader(null);
                }else {
                    super.draw(batch, parentAlpha);
                }
            }
        };
        levelImage.setOrigin(Align.center);
        levelImage.setImageName("trueImage");
        initView(index);
    }

    public ImageActor(TextureAtlas.AtlasRegion image, String index){
        levelImage = new Image(image){
            private float timeTotal = 0;

            @Override
            public void act(float delta) {
                super.act(delta);
                if (isAnimationStart) {
                    setPosition(hintGroup.getWidth() / 2, hintGroup.getHeight() / 2, Align.center);
                }
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (shaderProgram!=null) {
                    batch.setShader(shaderProgram);
                    int time1 = shaderProgram.getUniformLocation("contrast");
                    if (timeTotal >1){
                        MathUtils.clamp(timeTotal,0.0F,1);
                    }
                    shaderProgram.setUniformf(time1, timeTotal *0.6F);
                    super.draw(batch, parentAlpha);
                    timeTotal += Gdx.graphics.getDeltaTime();
                    batch.setShader(null);
                }else {
                    super.draw(batch, parentAlpha);
                }
            }
        };
        levelImage.setImageName("trueImage");
        initView(index);
    }

    public void setShaderType(ShaderType type){
        shaderProgram = ShaderManager.getManager().getType(type);
    }

    private void initView(String index) {
        setSize(100+120,100);
        touchWidth = 200;
        touchHight = 100;
        hintGroup = new Group();
        hintGroup.setSize(touchWidth,touchHight);

        addActor(hintGroup);
        hintGroup.addActor(levelImage);
        hintGroup.setPosition(getWidth()/2,getHeight()/2, Align.center);
        hintGroup.setOrigin(Align.center);
        baseImageX = hintGroup.getX();
        baseImageY = hintGroup.getY();
        baseWidth = levelImage.getWidth();
        baseHight = levelImage.getHeight();
        maxScale = Math.min(108.0f / baseHight, 192.0F/baseWidth);
        if ((maxScale <1)) {
            levelImage.setSize(
                    Math.min(baseWidth*maxScale, baseWidth),
                    Math.min(baseHight * maxScale, baseHight));
        }
        levelImage.setPosition(hintGroup.getWidth()/2,hintGroup.getHeight()/2,Align.center);
        setName(index);
    }

    public float getMaxScale() {
        return maxScale;
    }

    public float getBaseWidth() {
        return baseWidth;
    }

    public float getBaseHight() {
        return baseHight;
    }

    public void setListener(ClickListener listener){
        hintGroup.addListener(listener);
    }

    public void resetPosition() {
        levelImage.clearActions();
        hintGroup.addAction(Actions.moveTo(baseImageX,baseImageY,huifuTime*1.2F));


        if ((maxScale < 1)) {
//            levelImage.setSize(
//                    Math.min(baseWidth*maxScale, baseWidth),
//                    Math.min(baseHight * maxScale, baseHight));
            levelImage.addAction(Actions.sizeTo(Math.min(baseWidth, baseWidth * maxScale),
                    Math.min(baseHight * maxScale, baseHight),huifuTime*1.2F));
        }
        levelImage.addAction(Actions.sequence(Actions.moveToAligned(touchWidth/2,
                touchHight/2,Align.center,huifuTime*1.2F),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {

                    }
                })));
    }

    private Action action;

    public Action getAction() {
        return action = Actions.sizeTo(0, 100,weiSuiTime*2F);
    }

    public void startDragAnimation() {
        levelImage.clearActions();
        isAnimationStart = true;
        GameStaticInstance.gameListener.vibrate(25,-1);
        levelImage.addAction(Actions.delay(0.05f,Actions.sequence(
                Actions.parallel(
                Actions.sizeTo(baseWidth * LevelConfig.globalScale,
                        baseHight * LevelConfig.globalScale, weiSuiTime*1.7F)
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                       isAnimationStart  = false;
                    }
                })
        )));
        addAction(getAction());

        toFront();
    }

    public boolean isAnimationStart() {
        return isAnimationStart;
    }

    public void setAnimationStart(boolean animationStart) {
        this.isAnimationStart = animationStart;
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public void touchCancel(){
        hintGroup.clearActions();
        isAnimation = false;
    }

    public float getLevelImageX(){
        return hintGroup.getX(Align.center);
    }

    public float getLevelImageY(){
        return hintGroup.getY();
    }

    public void setLevelImagePosition(float x, float y) {
        hintGroup.addAction(Actions.moveToAligned(x- hintGroup.getWidth()/2,y,Align.center,weiSuiTime*0.6F));
    }

    public Group getHintGroup() {
        return hintGroup;
    }

    public Image getLevelImage() {
        return levelImage;
    }

    public void addRestAction() {
        removeAction(action);
        addAction(Actions.delay(0.1F,Actions.sizeTo(100+120, 100, huifuTime)));
//        setSize(100+120, 100);
    }

    @Override
    public void clearActions() {
        super.clearActions();
    }

    public void setAfterShow(Array<String> afterShow) {
        this.afterShow = afterShow;
    }

    public Array<String> getAfterShow() {
        return afterShow;
    }

    public void cancel() {
        resetPosition();
        touchCancel();
        addRestAction();
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
    }

    public void shanbai(float v) {
        if (v == 0){
            setShaderType(ShaderType.BrightNess);
        }else {
            setShaderType(ShaderType.BrightNess);
        }
    }

    //标记提示    如果滑动的是它就取消提示 并且还需要清除提示
    private int target;
    public void setTarget(int i) {
        this.target = i;
    }

}
