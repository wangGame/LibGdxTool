package com.libGdx.test.beser;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

public class BUL extends TemporalAction {
    private Array<Vector2> p;
    private Array<Vector2> allBezierPos;
    public BUL(Vector2 startV2, Vector2 contr1, Vector2 endV2, Vector2 contr2){
        p = new Array<>();
        p.add(startV2);
        p.add(contr1);
        p.add(endV2);
        p.add(contr2);
        allBezierPos = calculateBezierPoint();
    }

    @Override
    protected void update(float t) {
        int size = allBezierPos.size;
        Vector2 vector2 = allBezierPos.get((int) ((size-1) * t));
        target.setPosition(vector2);
    }

    // 计算所有贝塞尔曲线的点
    private Array<Vector2> calculateBezierPoint() {
        Array<Vector2> allBezierPos = new Array<>();
        float gap = 1 / 300f; // 每次迭代步长
        for (float i = 0; i <= 1; i += gap) {
            Vector2 pos = calculateBezierP(i);
            allBezierPos.add(pos);
        }
        return allBezierPos;
    }

    private Vector2 calculateBezierP(float t) {
        // 三阶贝塞尔运算
        float x = (float) (Math.pow(1 - t, 3) * p.get(0).x +
                3 * t * Math.pow(1 - t, 2) * p.get(1).x +
                3 * Math.pow(t, 2) * (1 - t) * p.get(2).x +
                Math.pow(t, 3) * p.get(3).x);

        float y = (float) (Math.pow(1 - t, 3) * p.get(0).y +
                3 * t * Math.pow(1 - t, 2) * p.get(1).y +
                3 * Math.pow(t, 2) * (1 - t) * p.get(2).y +
                Math.pow(t, 3) * p.get(3).y);

        return new Vector2(x, y);
    }


private float distance(Vector2 p1, Vector2 p2) {
        return (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

}
