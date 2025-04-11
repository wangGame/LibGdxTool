package com.libGdx.test.beser;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

public class BUL1 extends TemporalAction {
    private Array<Vector2> p;
    private Array<Vector2> allBezierPos;

    // 构造函数接受一个可变数量的控制点
    public BUL1(Array<Vector2> controlPoints) {
        p = new Array<>(controlPoints);
        allBezierPos = calculateBezierPoint();
    }

    @Override
    protected void update(float t) {
        int size = allBezierPos.size;
        Vector2 vector2 = allBezierPos.get((int) ((size - 1) * t));
        target.setPosition(vector2.x, vector2.y);
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

    // 计算n阶贝塞尔曲线的单个点
    private Vector2 calculateBezierP(float t) {
        float x = 0;
        float y = 0;
        int n = p.size - 1;

        // 计算贝塞尔曲线的x和y值
        for (int i = 0; i <= n; i++) {
            float binomialCoeff = binomialCoefficient(n, i); // 组合数
            float weight = (float) (Math.pow(1 - t, n - i) * Math.pow(t, i));
            x += p.get(i).x * binomialCoeff * weight;
            y += p.get(i).y * binomialCoeff * weight;
        }

        return new Vector2(x, y);
    }

    // 计算组合数 nCi
    private int binomialCoefficient(int n, int i) {
        if (i == 0 || i == n) {
            return 1;
        }
        return binomialCoefficient(n - 1, i - 1) + binomialCoefficient(n - 1, i);
    }
}
