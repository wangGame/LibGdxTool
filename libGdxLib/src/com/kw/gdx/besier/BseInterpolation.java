package com.kw.gdx.besier;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

/**
 * 主要用于spine动画
 */
public class BseInterpolation extends Interpolation {
    private float[] curves = new float[18];

    @Override
    public float apply(float a) {
        return getCurvePercent(a);
    }

    float cx1;
    float cy1;
    float cx2;
    float cy2;
    public BseInterpolation(float []floats){
        cx1 = floats[0];
        cy1 = floats[1];
        cx2 = floats[2];
        cy2 = floats[3];

    }

    public BseInterpolation(float cx1, float cy1, float cx2, float cy2){
        setCurve(cx1,cy1,cx2,cy2);
    }

    public void setCurve (float cx1, float cy1, float cx2, float cy2) {
        float tmpx = (-cx1 * 2 + cx2) * 0.03f, tmpy = (-cy1 * 2 + cy2) * 0.03f;
        float dddfx = ((cx1 - cx2) * 3 + 1) * 0.006f, dddfy = ((cy1 - cy2) * 3 + 1) * 0.006f;
        float ddfx = tmpx * 2 + dddfx, ddfy = tmpy * 2 + dddfy;
        float dfx = cx1 * 0.3f + tmpx + dddfx * 0.16666667f, dfy = cy1 * 0.3f + tmpy + dddfy * 0.16666667f;
        float x = dfx;
        float y = dfy;

        float curves[] = this.curves;
        int i = 0;
        for (int n = i + 19 - 1; i < n; i += 2) {
            curves[i] = x;
            curves[i + 1] = y;
            dfx += ddfx;
            dfy += ddfy;
            ddfx += dddfx;
            ddfy += dddfy;
            x += dfx;
            y += dfy;
        }

        System.out.println("-------------------");
    }


    //
//
    public float getCurvePercent (float percent) {
        percent = MathUtils.clamp(percent, 0, 1);
        float[] curves = this.curves;
        int i = 0;
        float x = 0;
        for (int start = i, n = i + 19 - 1; i < n; i += 2) {
            x = curves[i];
            if (x >= percent) {
                if (i == start) return curves[i + 1] * percent / x; // First point is 0,0.
                float prevX = curves[i - 2], prevY = curves[i - 1];
                return prevY + (curves[i + 1] - prevY) * (percent - prevX) / (x - prevX);
            }
        }
        float y = curves[i - 1];
        float v = y + (1 - y) * (percent - x) / (1 - x);




        return v;
    }

    @Override
    public String toString() {
        return "BseInterpolation{" +
                " " + cx1 + "F"+
                "," + cy1 +"F"+
                "," + cx2 +"F"+
                "," + cy2 +"F"+
                '}';
    }
}