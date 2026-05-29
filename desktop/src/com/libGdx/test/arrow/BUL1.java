package com.libGdx.test.arrow;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

public class BUL1 extends TemporalAction {
    private final Array<Vector2> controlPoints;
    private final Array<Vector2> tempPoints;
    private final Vector2 out = new Vector2();

    private float moveT = 0F;
    private float rawT = 0F;

    public BUL1(Array<Vector2> controlPoints) {
        this.controlPoints = new Array<>();
        this.tempPoints = new Array<>();
        for (Vector2 v : controlPoints) {
            this.controlPoints.add(new Vector2(v));
            this.tempPoints.add(new Vector2());
        }
    }

    @Override
    protected void update(float t) {
        moveT = t;

        float duration = getDuration();
        rawT = duration <= 0F ? 1F : Math.min(1F, getTime() / duration);

        valueAt(moveT, out);

        if (target != null) {
            target.setPosition(out.x, out.y);
        }
    }

    public float getMoveT() {
        return moveT;
    }

    public float getRawT() {
        return rawT;
    }

    /**
     * 根据 t 计算贝塞尔曲线上的点。
     */
    public Vector2 valueAt(float t, Vector2 out) {
        if (t < 0F) t = 0F;
        if (t > 1F) t = 1F;

        calculateBezierP(t, out);
        return out;
    }

    private void calculateBezierP(float t, Vector2 out) {
        int count = controlPoints.size;

        if (count == 0) {
            out.set(0F, 0F);
            return;
        }

        if (count == 1) {
            out.set(controlPoints.get(0));
            return;
        }

        for (int i = 0; i < count; i++) {
            tempPoints.get(i).set(controlPoints.get(i));
        }

        // De Casteljau 算法
        for (int level = count - 1; level > 0; level--) {
            for (int i = 0; i < level; i++) {
                Vector2 a = tempPoints.get(i);
                Vector2 b = tempPoints.get(i + 1);

                a.x = a.x + (b.x - a.x) * t;
                a.y = a.y + (b.y - a.y) * t;
            }
        }

        out.set(tempPoints.get(0));
    }
}