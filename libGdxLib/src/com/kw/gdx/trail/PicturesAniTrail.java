package com.kw.gdx.trail;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PicturesAniTrail extends PictureTrail {

    private TextureRegion[] regions;
    private int currentIndex = 0;
    private float frameTime = 0f;
    private float frameDuration = 0.1f;

    public void setRegions(TextureRegion[] regions) {
        this.regions = regions;
        this.currentIndex = 0;
        this.frameTime = 0f;
    }

    public void setFrameDuration(float time) {
        this.frameDuration = time;
    }

    @Override
    public void reset() {
        super.reset();
        this.regions = null;
        this.currentIndex = 0;
        this.frameDuration = 0.1f;
    }


    //根据每多少时间换一次图片
    @Override
    public void act(float delta) {
        if (this.regions != null && this.regions.length > 0) {
            this.frameTime += Math.max(delta, 0f);
            if(this.frameTime >= this.frameDuration) {
                this.currentIndex++;
                if(this.currentIndex >= this.regions.length) {
                    this.currentIndex = 0;
                }
                this.frameTime %= this.frameDuration;
            }
            setRegion(this.regions[this.currentIndex]);
        }
        super.act(delta);
    }
}
