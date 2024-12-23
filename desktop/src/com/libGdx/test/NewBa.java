package com.libGdx.test;

import com.badlogic.gdx.math.Interpolation;

public class NewBa extends Interpolation {
    private   float[] curves =new float[19];
    /**
     *
     * @param time1 0.5333F   float time = keyMap.getFloat("time", 0);
     * @param value1 	float value1 = keyMap.getFloat(name1, defaultValue) * scale, value2 = keyMap.getFloat(name2, defaultValue) * scale;   x y   默认为   0
     * @param cx1
     * @param cy1
     * @param cx2
     * @param cy2
     * @param time2 nexttime  endtime?  float time2 = nextMap.getFloat("time", 0);
     * @param value2 deault 0 float nvalue1 = nextMap.getFloat(name1, defaultValue) * scale, nvalue2 = nextMap.getFloat(name2, defaultValue) * scale;  x  y   目标
     */
    public void setBezier (float time1, float value1, float cx1, float cy1, float cx2,
                           float cy2, float time2, float value2) {

        float tmpx = (time1 - cx1 * 2 + cx2) * 0.03f, tmpy = (value1 - cy1 * 2 + cy2) * 0.03f;
        float dddx = ((cx1 - cx2) * 3 - time1 + time2) * 0.006f, dddy = ((cy1 - cy2) * 3 - value1 + value2) * 0.006f;
        float ddx = tmpx * 2 + dddx, ddy = tmpy * 2 + dddy;
        float dx = (cx1 - time1) * 0.3f + tmpx + dddx * 0.16666667f, dy = (cy1 - value1) * 0.3f + tmpy + dddy * 0.16666667f;
        float x = time1 + dx, y = value1 + dy;
        int i = 0;
        for (int n = 17; i < n; i += 2) {
            curves[i] = x;
            curves[i + 1] = y;
            dx += ddx;
            dy += ddy;
            ddx += dddx;
            ddy += dddy;
            x += dx;
            y += dy;
        }
    }

    @Override
    public float apply(float a) {
        return getBezierValue(a);
    }


    public float getBezierValue (float time) {
        int i = 0;
        float[] curves = this.curves;
        if (curves[i] > time) {
            float x =0, y = 0;
            return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
        }
        int n = 2;
        for (i += 2; i < n; i += 2) {
            if (curves[i] >= time) {
                float x = curves[i - 2], y = curves[i - 1];
                return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
            }
        }
        float x = curves[n - 2], y = curves[n - 1];
        float v = y + (time - x) / (- x) * ( - y);
        System.out.println(v+"==============");
        return v;
    }

}
