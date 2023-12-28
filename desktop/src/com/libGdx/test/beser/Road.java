package com.libGdx.test.beser;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/17 14:00
 */
public class Road {
    private final int STEP = 5;
    Array<float[]> pos = new Array<float[]>();

    public void initPoint(Vector2[] points){
        pos.clear();
        getBezierPoints(pos, points, STEP,false);
    }

    public static void getBossLinePoints(Array<float[]> pos, Vector2[] points, int step) {
        for (int p = 0; p < points.length - 1; p++) {
            Vector2 sp = points[p];
            Vector2 ep = points[p + 1];
            //限定为水平或者垂直线
            if (Math.abs(sp.y - ep.y) < 1) { // 水平线
                float len = ep.x - sp.x;
                float x = sp.x;
                float r = 0;
                if (len < 0) {
                    r = 180;
                    while (x > ep.x) {
                        pos.add(new float[] { x, sp.y, r });
                        x -= step;
                    }
                } else {
                    while (x < ep.x) {
                        pos.add(new float[] { x, sp.y, r });
                        x += step;
                    }
                }
            } else { //垂直线
                float len = ep.y - sp.y;
                float y = sp.y;
                float r = 90;
                if (len < 0) {
                    r = 270;
                    while (y > ep.y) {
                        pos.add(new float[] { sp.x, y, r });
                        y -= step;
                    }
                } else {
                    while (y < ep.y) {
                        pos.add(new float[] { sp.x, y, r });
                        y += step;
                    }
                }
            }
        }
    }

    public static void getBezierPoints(Array<float[]> points, Vector2[] rp, float pointSize,boolean isVertical) {
        if (rp == null) {
            return;
        }

        Vector2 p0 = new Vector2(0, 0);
        Vector2 p1 = new Vector2(0, 0);
        Vector2 p2 = new Vector2(0, 0);

        for (int j = 0; j < rp.length - 2; ++j) {
            if (j == 0) {
                p0.x = rp[0].x;
                p0.y = rp[0].y;
            } else {
                p0.x = (rp[j].x + rp[j + 1].x) / 2;
                p0.y = (rp[j].y + rp[j + 1].y) / 2;
            }
            p1.x = rp[j + 1].x;
            p1.y = rp[j + 1].y;
            if (j <= rp.length - 4) {
                p2.x = (rp[j + 1].x + rp[j + 2].x) / 2;
                p2.y = (rp[j + 1].y + rp[j + 2].y) / 2;
            } else {
                p2.x = rp[j + 2].x;
                p2.y = rp[j + 2].y;
            }
            if ((p2.x - p0.x) * (p1.y - p0.y) - (p2.y - p0.y) * (p1.x - p0.x) == 0) {
                p1.x++;
                p1.y++;
            }
            int steps = RBBezierUtil.init(p0, p1, p2, pointSize);
            if (steps == 0) {
//                LogU.log("steps:" + steps + " " + p0 + p1 + p2);
            }
            for (int m = 1; m <= steps; ++m) {
                float[] data = RBBezierUtil.getAnchorPointRB(m,isVertical);
                if (data != null) {
                    points.add(data);
                }
            }
        }
    }

    public Array<float[]> getDrawPos(int sizeP) {
        int size = (int) (pos.size * sizeP/100.0f);
        Array<float[]> drawPos = new Array<float[]>(size);
        for (int i = 0; i < size; i += 3) {
            drawPos.add(cpy(pos.get(i)));
        }
        if (size % 3 != 1) {
            drawPos.add(cpy(pos.peek()));
        }
        return drawPos;
    }

    private float[] cpy(float[] arr) {
        float[] pos = new float[arr.length];
        System.arraycopy(arr, 0, pos, 0, arr.length);
        return pos;
    }
}
