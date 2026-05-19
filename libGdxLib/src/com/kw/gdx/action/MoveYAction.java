package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;

/** Moves an actor from its current position to a specific position.
 * @author Nathan Sweet */
public class MoveYAction extends TemporalAction {
    private float startY;
    private float endY;
    private int alignment = Align.bottomLeft;

    protected void begin () {
        startY = target.getY(alignment);
    }

    protected void update (float percent) {
        float y;
        if (percent == 0) {
            y = startY;
        } else if (percent == 1) {
            y = endY;
        } else {
            y = startY + (endY - startY) * percent;
        }
        target.setY(y, alignment);
    }

    public void reset () {
        super.reset();
        alignment = Align.bottomLeft;
    }

    public void setY (float y) {
        endY = y;
    }

    public float getY () {
        return endY;
    }

    public int getAlignment () {
        return alignment;
    }

    public void setAlignment (int alignment) {
        this.alignment = alignment;
    }
}
