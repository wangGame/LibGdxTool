package com.libGdx.test;


import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;

public class BezierMoveAction extends TemporalAction {
    private int align;
    private Bezier<Vector2> vector2Bezier;
    Vector2 tempPosition = new Vector2();

    public void setBezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        setBezier(new Vector2(x1, y1), new Vector2(x2, y2), new Vector2(x3, y3), new Vector2(x4, y4));
    }

    public void setBezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int align) {
        setBezier(new Vector2(x1, y1), new Vector2(x2, y2), new Vector2(x3, y3), new Vector2(x4, y4), align);
    }

    public void setBezier(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4) {
        setBezier(v1, v2, v3, v4, Align.bottomLeft);
    }

    public void setBezier(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4, int align) {
        vector2Bezier = new Bezier<>();
        vector2Bezier.set(v1, v2, v3, v4);
        this.align = align;
    }

    @Override
    protected void update(float percent) {
        tempPosition = vector2Bezier.valueAt(tempPosition, percent);
        actor.setPosition(tempPosition.x, tempPosition.y, align);
    }
}
