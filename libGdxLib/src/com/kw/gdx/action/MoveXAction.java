package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;

/** Moves an actor from its current position to a specific position.
 * @author Nathan Sweet */
public class MoveXAction extends TemporalAction {
    private float startX;
    private float endX;
    private int alignment = Align.bottomLeft;

    protected void begin () {
        startX = target.getX(alignment);
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
        target.setX(x, alignment);
    }

    public void reset () {
        super.reset();
        alignment = Align.bottomLeft;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }
}
