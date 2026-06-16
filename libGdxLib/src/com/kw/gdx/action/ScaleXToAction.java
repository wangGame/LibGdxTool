package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ScaleXToAction extends TemporalAction {
    private float startX;
    private float endX;

    protected void begin () {
        startX = target.getScaleX();
    }

    protected void update (float percent) {
        float x;
        if (percent == 0) {
            x = startX;
        } else if (percent == 1) {
            x = endX;
        } else {
            x = startX + (endX - startX) * percent;
        }
        target.setScaleX(x);
    }

    public void setScale (float scale) {
        endX = scale;
    }

    public float getX () {
        return endX;
    }

    public void setX (float x) {
        this.endX = x;
    }
}
