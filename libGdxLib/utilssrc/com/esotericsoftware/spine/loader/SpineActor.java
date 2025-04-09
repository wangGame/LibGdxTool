package com.esotericsoftware.spine.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.kw.gdx.asset.Asset;

public class SpineActor extends Actor {
    protected Skeleton skeleton;
    private AnimationState state;
    private SkeletonRenderer renderer;
    private AnimationStateData animData;
    private String path;
    private float rootBoneScaleX  = 1,rootBoneScaleY=1;
    private float offsetX,offsetY;
    private AssetManager assetamnagerinstance;
    private boolean active;

    public SpineActor(String path) {
        this.path = path;
        flushAsset();
        init(path);
    }

    public String getPath() {
        return path;
    }

    public SpineActor(String path, boolean flag) {
        this.path = path;
        flushAsset();
        init(path);
    }

    public SpineActor(String path, String atlas) {
        this.path = path;
        assetamnagerinstance = Asset.getAsset().getAssetManager();
        if(!assetamnagerinstance.isLoaded(path+".json")) {
            SkeletonDataLoader.SkeletonDataParameter mainSkeletonParameter = new SkeletonDataLoader.SkeletonDataParameter();
            mainSkeletonParameter.atlasName = atlas;
            assetamnagerinstance.load(path + ".json", SkeletonData.class,mainSkeletonParameter);
            assetamnagerinstance.finishLoading();
        }
        init(path);
    }

    public SpineActor(String path, String atlas,AssetManager assetManager) {
        this.path = path;
        assetamnagerinstance = assetManager;
        if(!assetamnagerinstance.isLoaded(path+".json")) {
            assetManager.load(atlas, TextureAtlas.class);
            assetManager.finishLoading();
            SkeletonDataLoader.SkeletonDataParameter mainSkeletonParameter = new SkeletonDataLoader.SkeletonDataParameter();
            mainSkeletonParameter.atlasName = atlas;
            assetamnagerinstance.load(path + ".json", SkeletonData.class,mainSkeletonParameter);
            assetamnagerinstance.finishLoading();
        }
        init(path);
    }

    public void flushAsset(){
        assetamnagerinstance = Asset.getAsset().getAssetManager();
        if(!assetamnagerinstance.isLoaded(path+".json")) {
            assetamnagerinstance.load(path + ".json", SkeletonData.class);
            assetamnagerinstance.finishLoading();
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void setColor(Color color) {
        skeleton.setColor(color);
        super.setColor(color);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        skeleton.getRootBone().setPosition(width/2,height/2);
    }

    public void init(String path) {
        renderer = Asset.getAsset().getRenderer();
        SkeletonData data = assetamnagerinstance.get(path+".json");
        skeleton = new Skeleton(data);
        animData = new AnimationStateData(data);
        state = new AnimationState(animData);
        rootBoneScaleX = skeleton.getRootBone().getScaleX();
        rootBoneScaleY = skeleton.getRootBone().getScaleY();
    }

    public void setAnimation(String name, boolean loop){
        state.setAnimation(0,name,loop);
    }

    public void setSpineOffset(float offsetX,float offsetY){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void setAnimation(String name,String name2){
        setAnimation(name,false);
        getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                setAnimation(name2,true);
            }
        });
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        skeleton.getRootBone().setScale(scaleXY);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        skeleton.setPosition(getX() + offsetX,getY() + offsetY);
    }

    @Override
    public void setRotation(float degrees) {
        skeleton.getRootBone().setRotation(degrees);
        super.setRotation(degrees);
    }

    public void setSkin(String name){
        skeleton.setSkin(name);
        skeleton.setSlotsToSetupPose();
    }

    public void setAttachment(String s, String s1){
        skeleton.setAttachment(s,s1);
    }


    public void dispose(){
        remove();
    }


    public void unloadSpine(){
        if(Asset.getAsset().getAssetManager().isLoaded(path+".json")){
            Asset.getAsset().getAssetManager().unload(path+".json");
        }
        remove();
    }

    private boolean isClip;
    private float w;
    private float h;

    @Override
    public void addAction(Action action) {
        super.addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float alpha = this.getColor().a*parentAlpha;
        Color color = skeleton.getColor();
        float oldAlpha = color.a;
        skeleton.getColor().a *= alpha;
        if (customize){
            skeleton.getRootBone().setScale(rootBoneScaleX*getScaleX(),
                    getScaleY()*rootBoneScaleY);
        }

        state.update(Gdx.graphics.getDeltaTime());
        state.apply(skeleton);
        skeleton.updateWorldTransform();
        int src = batch.getBlendSrcFunc();
        int dst = batch.getBlendDstFunc();
        if (isClip) {
            batch.flush();
            if (clipBegin(getX() + beginX,getY() + beginY , w, h)) {
                renderer.draw(batch, skeleton);
                batch.flush();
                clipEnd();
            }
        }else {
            renderer.draw(batch, skeleton);
        }

        batch.setBlendFunction(src,dst);
        color.a = oldAlpha;
    }

    public void setH(float h) {
        this.h = h;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setClip(boolean clip) {
        isClip = clip;
    }

    @Override
    public Stage getStage() {
        return super.getStage();
    }

    public AnimationState getAnimaState(){
        return state;
    }
    public SkeletonData getsData() {
        return skeleton.getData();
    }

    private boolean customize = true;
    public void setCustomizeScale(boolean b) {
        this.customize = b;
    }

    public void setTimeScale(float timeScale){
        state.setTimeScale(timeScale);
    }

    public void completeDispose() {
        getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                super.complete(entry);
                remove();
            }
        });
    }

    public void completeRomove() {
        getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                super.complete(entry);
                remove();
            }
        });
    }

    public void setFlipX(){
//        skeleton.setFlipX(false);
//        skeleton.setFlipY(false);
    }

    private float beginY;
    public void setBeginY(int i) {
        this.beginY = i;
    }

    private float beginX;
    public void setBeginX(int i) {
        this.beginX = i;
    }
}
