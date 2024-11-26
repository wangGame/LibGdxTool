package com.libGdx.test.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.animation.effect.EffectTool;
import com.libGdx.test.base.LibGdxTestMain;

public class EffectPoolTest extends LibGdxTestMain {
    ParticleEffectPool touchEffectPool;
    public static void main(String[] args) {
        EffectPoolTest test = new EffectPoolTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);





        ParticleEffect touchEffect = new ParticleEffect();
        touchEffect.load(Gdx.files.internal("UdacityEmitter.p"), Gdx.files.internal(""));
        touchEffect.setEmittersCleanUpBlendFunction(false);
        touchEffectPool = new ParticleEffectPool(touchEffect, 1, 2);

        ParticleEffectPool.PooledEffect effect = touchEffectPool.obtain();
        effect.setPosition(0, Gdx.graphics.getHeight() );
        if (effect.isComplete()) {

        }
    }
}
