package com.libGdx.test.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/29 18:41
 */

public class Trail {
    private ParticleEffect particleEffect;
    private Array<ParticleEmitter> emitters;

    public Trail() {
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("path/to/particle/effect"),
                Gdx.files.internal("path/to/particle/images"));
        emitters = particleEffect.getEmitters();
    }

    public void update(float deltaTime) {
        particleEffect.update(deltaTime);
    }

    public void render(Batch spriteBatch) {
        particleEffect.draw(spriteBatch);
    }

    public void setPosition(float x, float y) {
        for (ParticleEmitter emitter : emitters) {
            emitter.setPosition(x, y);
        }
    }

    public void start() {
        particleEffect.start();
    }

    public void stop() {
        particleEffect.allowCompletion();
    }
}
