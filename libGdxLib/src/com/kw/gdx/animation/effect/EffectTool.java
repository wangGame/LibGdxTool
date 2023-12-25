package com.kw.gdx.animation.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.log.NLog;

public class EffectTool extends Actor {
    private ParticleEffect effect;
    private String effectResourcePath;
    private AssetManager assetamnagerinstance;
    private float clipH;
    private float clipW;
    private boolean loop;
    private boolean isClip = false;

    public EffectTool(String effectResourcePath){
        this.effectResourcePath = effectResourcePath;
        assetamnagerinstance = Asset.assetManager;
        if (!assetamnagerinstance.isLoaded(effectResourcePath)){
            assetamnagerinstance.load(effectResourcePath, ParticleEffect.class);
            assetamnagerinstance.finishLoading();
        }
        init();
    }

    public EffectTool(String path, String atlasFile){
        this.effectResourcePath = path;
        assetamnagerinstance = Asset.assetManager;
        if (!assetamnagerinstance.isLoaded(path)){
            ParticleEffectLoader.ParticleEffectParameter
                    particleEffectParameter =
                    new ParticleEffectLoader.ParticleEffectParameter();
            particleEffectParameter.atlasFile = atlasFile;
            assetamnagerinstance.load(path, ParticleEffect.class,particleEffectParameter);
            assetamnagerinstance.finishLoading();
        }
        init();
    }

    public void setEffectScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        //缩放粒子  回到池中恢复     motionScaleFactor发射器的速度
        effect.scaleEffect(scaleX,scaleY,1);
    }

    public void init(){
        effect = assetamnagerinstance.get(effectResourcePath);
        effect = new ParticleEffect(effect);
        play();
    }

    public void play(){
        effect.reset();
        effect.start();
    }

    public void setClipW(float clipW) {
        this.clipW = clipW;
    }

    public void setClipH(float clipH) {
        this.clipH = clipH;
    }


    public ParticleEffect getEffect() {
        return effect;
    }

    public boolean isComplate(){
        return effect.isComplete();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        effect.setPosition(x,y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        effect.setPosition(x,y);
        if (effect.isComplete()) {
            if (loop){
                play();
            }
        }
    }

    public void setClip(boolean flag) {
        this.isClip= flag;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible())return; //duo yu
        int blendSrcFunc = batch.getBlendSrcFunc();
        int blendDstFunc = batch.getBlendDstFunc();
        if (isClip) {
            batch.flush();
            if (clipBegin(0, 0, clipW, clipH)) {
                effect.draw(batch, Gdx.graphics.getDeltaTime());
                batch.flush();
                clipEnd();
            }
        }else {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
        }

        batch.setBlendFunction(blendSrcFunc,blendDstFunc);
    }

    public void dispose(){
        try {
            assetamnagerinstance.unload(effectResourcePath);
        }catch (Exception e){
            NLog.e("dispose error !");
        }
    }

    @Override
    public void setRotation(float degrees) {
        super.setRotation(degrees);
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /**
     * 水平反转
     */
    public void setFlipX(){
        effect.flipX();
    }

    /**
     * 竖直反转
     */
    public void setFlipY(){
        effect.flipY();
    }
}

