package com.libGdx.test.beser;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

public class BU extends TemporalAction {
    private Array<Vector2> p;
    public BU(Vector2 startV2,Vector2 contr1,Vector2 endV2,Vector2 contr2){
        p = new Array<>();
        p.add(startV2);
        p.add(contr1);
        p.add(endV2);
        p.add(contr2);
    }

    @Override
    protected void update(float t) {
        // 三阶贝塞尔运算
        float x = (float) (Math.pow(1 - t, 3) * p.get(0).x +
                3 * t * Math.pow(1 - t, 2) * p.get(1).x +
                3 * Math.pow(t, 2) * (1 - t) * p.get(2).x +
                Math.pow(t, 3) * p.get(3).x);

        float y = (float) (Math.pow(1 - t, 3) * p.get(0).y +
                3 * t * Math.pow(1 - t, 2) * p.get(1).y +
                3 * Math.pow(t, 2) * (1 - t) * p.get(2).y +
                Math.pow(t, 3) * p.get(3).y);
        target.setPosition(x,y);
    }

    // 计算贝塞尔曲线的长度
    private float calculateBezierLength() {
        float totalLineLen = 0;
        for (int i = 1; i < p.size; ++i) {
            Vector2 currentPos = p.get(i);
            Vector2 previousPos = p.get(i - 1);
            totalLineLen += distance(currentPos, previousPos);
        }
        return totalLineLen;
    }

    private float distance(Vector2 p1, Vector2 p2) {
        return (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

}
