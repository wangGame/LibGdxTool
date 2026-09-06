package com.kw.gdx.cirprogress;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class ProcessGroupLine extends Group {
    private Image progressBg;
    private Image progressFg;

    public ProcessGroupLine(Image progressBg, Image progressFg) {
        this.progressBg = progressBg;
        this.progressFg = progressFg;
        addActor(progressBg);
        addActor(progressFg);
        setSize(progressBg.getWidth(), progressBg.getHeight());
        progressBg.setPosition(getWidth() / 2.0f, getHeight() / 2.0f, Align.center);
    }

    public void setFgPosition(float x, float y) {
        progressFg.setPosition(x, y);
    }

    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > 1) {
            progress = 1;
        }
        progressFg.setWidth(progress * progressBg.getWidth());
    }
}
