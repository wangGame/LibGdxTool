package com.libGdx.test.clip;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.drawable.PageFlipDrawable;

public class PageFlipImage extends Image {

    private final PageFlipDrawable pageFlipDrawable;

    private float time = 0f;
    private float duration = 0.8f;
    private boolean playing = false;

    public PageFlipImage(PageFlipDrawable drawable) {
        super(drawable);
        this.pageFlipDrawable = drawable;
    }

    public void playFlip() {
        time = 0f;
        playing = true;
        pageFlipDrawable.setProgress(0f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!playing) return;

        time += delta;

        float progress = time / duration;

        if (progress >= 1f) {
            progress = 1f;
            playing = false;
        }

        /**
         * smoother 效果比 linear 更像翻页。
         */
        progress = Interpolation.smoother.apply(progress);

        pageFlipDrawable.setProgress(progress);
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}