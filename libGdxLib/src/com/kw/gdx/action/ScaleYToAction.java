package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ScaleYToAction extends TemporalAction {
    private float startY;
    private float endY;

    protected void begin () {
        startY = target.getScaleY();
    }

    protected void update (float percent) {
        float y;
        if (percent == 0) {
            y = startY;
        } else if (percent == 1) {
            y = endY;
        } else {
            y= startY + (endY - startY) * percent;
        }
        target.setScaleY(y);
    }

    public void setScale (float scale) {
        endY = scale;
    }

    public float getX () {
        return endY;
    }

    public void setX (float x) {
        this.endY = x;
    }
}
