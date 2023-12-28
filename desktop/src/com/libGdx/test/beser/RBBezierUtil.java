package com.libGdx.test.beser;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kw.gdx.constant.Constant;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/17 14:03
 */

public class RBBezierUtil {

    static Vector2 bezierP0 = new Vector2(0, 0);
    static Vector2 bezierP1 = new Vector2(0, 0);
    static Vector2 bezierP2 = new Vector2(0, 0);

    static float ax, ay, bx, by, A, B, C, totalLength;
    static int step;

    public static int init(Vector2 $p0, Vector2 $p1, Vector2 $p2, float $speed) {
        bezierP0.x = $p0.x;
        bezierP0.y = $p0.y;
        bezierP1.x = $p1.x;
        bezierP1.y = $p1.y;
        bezierP2.x = $p2.x;
        bezierP2.y = $p2.y;
        // step = 30;

        ax = bezierP0.x - 2 * bezierP1.x + bezierP2.x;
        ay = bezierP0.y - 2 * bezierP1.y + bezierP2.y;
        bx = 2 * bezierP1.x - 2 * bezierP0.x;
        by = 2 * bezierP1.y - 2 * bezierP0.y;

        A = 4 * (ax * ax + ay * ay);
        B = 4 * (ax * bx + ay * by);
        C = bx * bx + by * by;

        totalLength = L(1);

        //LogU.log("ax:"+ax+" ay:"+ay+" A:"+A);
        //LogU.log("total_length:"+total_length);

        step = MathUtils.floorPositive((float) (totalLength / $speed));
        if (totalLength % $speed > $speed / 2)
            step++;
        return step;
    }

    public static float[] getAnchorPointRB(float nIndex,boolean isVertical) {
        float[] array = new float[3];
        if (nIndex >= 0 && nIndex <= step) {
            float t = nIndex / step;
            float l = t * totalLength;

            t = InvertL(t, l);

            float xx = (1 - t) * (1 - t) * bezierP0.x + 2 * (1 - t) * t * bezierP1.x + t
                    * t * bezierP2.x;
            float yy = (1 - t) * (1 - t) * bezierP0.y + 2 * (1 - t) * t * bezierP1.y + t
                    * t * bezierP2.y;

            PointVector Q0 = new PointVector((int) ((1 - t) * bezierP0.x + t * bezierP1.x),
                    (int) ((1 - t) * bezierP0.y + t * bezierP1.y));
            PointVector Q1 = new PointVector((int) ((1 - t) * bezierP1.x + t * bezierP2.x),
                    (int) ((1 - t) * bezierP1.y + t * bezierP2.y));

            float dx = Q1.x - Q0.x;
            float dy = Q1.y - Q0.y;
            float radians = MathUtils.atan2(dy, dx);
            float degrees = radians * 180 / MathUtils.PI;

            if(isVertical)
            {
                array[0] = yy;
                array[1] = Constant.GAMEHIGHT - xx;
                array[2] = degrees - 90;
            }
            else {
                array[0] = xx;
                array[1] = yy;
                array[2] = degrees;
            }
            return array;
        } else {
            return null;
        }
    }

    private static float L(float t) {
        float temp1 = (float) Math.sqrt(C + t * (B + A * t));
        float temp2 = (float) (2 * A * t * temp1 + B * (temp1 - Math.sqrt(C)));
        float temp3 = (float) Math.log(B + 2 * Math.sqrt(A) * Math.sqrt(C));
        float temp4 = (float) Math.log(B + 2 * A * t + 2 * Math.sqrt(A) * temp1);
        float temp5 = (float) (2 * Math.sqrt(A) * temp2);
        float temp6 = (B * B - 4 * A * C) * (temp3 - temp4);

        return (float) ((temp5 + temp6) / (8 * Math.pow(A, 1.5)));
    }

    private static float s(float t) {
        return (float) Math.sqrt(A * t * t + B * t + C);
    }

    private static float InvertL(float t, float l) {
        float t1 = t;
        float t2 = 0;
        for (int i = 0; i < 100000; i++) {
            t2 = t1 - (L(t1) - l) / s(t1);
            if (Math.abs(t1 - t2) < 0.0002)
                break;
            t1 = t2;
        }
        return t2;
    }
}
