package com.libGdx.test.beser;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class BUL2 extends TemporalAction {

    private final Array<Vector2> controlPoints;
    private final Array<Vector2> tempPoints = new Array<>();
    private final Vector2 out = new Vector2();

    public BUL2(Array<Vector2> controlPoints) {
        this.controlPoints = new Array<>();

        // 拷贝控制点，避免外部修改影响曲线
        for (Vector2 v : controlPoints) {
            this.controlPoints.add(new Vector2(v));
            this.tempPoints.add(new Vector2());
        }
    }

    @Override
    protected void update(float t) {
        calculateBezierP(t, out);
        target.setPosition(out.x, out.y, Align.center);
    }

    /**
     * 使用 De Casteljau 算法动态计算任意阶贝塞尔点
     */
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

        // 先把控制点复制到临时数组
        for (int i = 0; i < count; i++) {
            tempPoints.get(i).set(controlPoints.get(i));
        }

        // De Casteljau 动态插值
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