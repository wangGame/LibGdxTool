package com.libGdx.test.colorcircle;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.asset.Asset;

public class ConnectionCircle extends Actor {

    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> animation2;
    private float time;

    public ConnectionCircle() {
        Texture texture = Asset.getAsset().getTexture("ui/loading.png");
        TextureRegion[] regions = TextureUtils.split(texture, 153, 150);
        animation = new Animation<>(0.03f,regions );
        animation2 = new Animation<>(0.04f,regions );
        animation2.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = animation.getKeyFrame(time, true);
        TextureRegion frame2 = animation2.getKeyFrame(time, true);
        float w = frame.getRegionWidth();
        float h= frame.getRegionHeight();
        batch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE_MINUS_SRC_COLOR);
        batch.draw(frame, getX()-w/2, getY()-h/2);
        batch.draw(frame2, getX()-w, getY()-h,frame.getRegionWidth()*2,frame.getRegionHeight()*2);
        batch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE);
        batch.draw(frame, getX()-w/2, getY()-h/2);
        batch.draw(frame2, getX()-w, getY()-h,frame.getRegionWidth()*2,frame.getRegionHeight()*2);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.flush();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
    }
}
