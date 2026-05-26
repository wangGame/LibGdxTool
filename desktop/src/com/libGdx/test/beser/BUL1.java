package com.libGdx.test.beser;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

public class BUL1 extends TemporalAction {

    private final Array<Vector2> controlPoints;
    private final Array<Vector2> tempPoints = new Array<>();
    private final Vector2 out = new Vector2();

    // 当前运动用的 t，受 Interpolation 影响
    private float moveT = 0;

    // 当前真实时间进度，不受 Interpolation 影响
    private float rawT = 0;

    public BUL1(Array<Vector2> controlPoints) {
        this.controlPoints = new Array<>();

        for (Vector2 v : controlPoints) {
            this.controlPoints.add(new Vector2(v));
            this.tempPoints.add(new Vector2());
        }
    }

    @Override
    protected void update(float t) {
        // t 是 TemporalAction 处理后的 t
        // 如果设置了 Interpolation.sineOut，这里的 t 已经不是线性的
        moveT = t;

        float duration = getDuration();
        rawT = duration <= 0 ? 1F : Math.min(1F, getTime() / duration);

        valueAt(moveT, out);
        target.setPosition(out.x, out.y);
    }

    /**
     * 当前运动进度，受 Interpolation 影响。
     * 用它画 debug，线头会和物体位置一致。
     */
    public float getMoveT() {
        return moveT;
    }

    /**
     * 当前真实时间进度，不受 Interpolation 影响。
     */
    public float getRawT() {
        return rawT;
    }

    /**
     * 外部 debug 绘制用：根据 t 动态求贝塞尔曲线上的点
     */
    public Vector2 valueAt(float t, Vector2 out) {
        if (t < 0) t = 0;
        if (t > 1) t = 1;

        calculateBezierP(t, out);
        return out;
    }

    private void calculateBezierP(float t, Vector2 out) {
        int count = controlPoints.size;

        if (count == 0) {
            out.set(0, 0);
            return;
        }

        if (count == 1) {
            out.set(controlPoints.get(0));
            return;
        }

        for (int i = 0; i < count; i++) {
            tempPoints.get(i).set(controlPoints.get(i));
        }

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