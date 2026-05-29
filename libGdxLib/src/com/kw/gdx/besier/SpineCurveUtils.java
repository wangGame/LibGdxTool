package com.kw.gdx.besier;


public final class SpineCurveUtils {
    public static final int BEZIER_SIZE = 18;

    private static final float STEP = 0.1f;
    private static final float STEP_SQUARED_TIMES_THREE = 0.03f;
    private static final float STEP_CUBED_TIMES_SIX = 0.006f;
    private static final float STEP_TIMES_THREE = 0.3f;
    private static final float ONE_OVER_SIX = 0.16666667f;

    private SpineCurveUtils() {
    }

    public static float getCubicBezierValue(float t, float value0, float value1, float value2, float value3) {
        float oneMinusT = 1 - t;
        float oneMinusTSquared = oneMinusT * oneMinusT;
        float tSquared = t * t;
        return oneMinusTSquared * oneMinusT * value0
                + 3 * oneMinusTSquared * t * value1
                + 3 * oneMinusT * tSquared * value2
                + tSquared * t * value3;
    }

    public static float[] buildSpineBezier(float time1, float value1, float cx1, float cy1, float cx2, float cy2,
                                           float time2, float value2) {
        float[] curves = new float[BEZIER_SIZE];
        setSpineBezier(curves, time1, value1, cx1, cy1, cx2, cy2, time2, value2);
        return curves;
    }

    public static void setSpineBezier(float[] curves, float time1, float value1, float cx1, float cy1, float cx2,
                                      float cy2, float time2, float value2) {
        if (curves == null || curves.length < BEZIER_SIZE) {
            throw new IllegalArgumentException("curves length must be at least " + BEZIER_SIZE);
        }

        int i = 0;
        float tmpx = (time1 - cx1 * 2 + cx2) * STEP_SQUARED_TIMES_THREE;
        float tmpy = (value1 - cy1 * 2 + cy2) * STEP_SQUARED_TIMES_THREE;
        float dddx = ((cx1 - cx2) * 3 - time1 + time2) * STEP_CUBED_TIMES_SIX;
        float dddy = ((cy1 - cy2) * 3 - value1 + value2) * STEP_CUBED_TIMES_SIX;
        float ddx = tmpx * 2 + dddx;
        float ddy = tmpy * 2 + dddy;
        float dx = (cx1 - time1) * STEP_TIMES_THREE + tmpx + dddx * ONE_OVER_SIX;
        float dy = (cy1 - value1) * STEP_TIMES_THREE + tmpy + dddy * ONE_OVER_SIX;
        float x = time1 + dx;
        float y = value1 + dy;
        for (int n = BEZIER_SIZE; i < n; i += 2) {
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

    public static float getSpineBezierValue(float time, float time1, float value1, float cx1, float cy1, float cx2,
                                            float cy2, float time2, float value2) {
        float[] curves = buildSpineBezier(time1, value1, cx1, cy1, cx2, cy2, time2, value2);
        return getSpineBezierValue(curves, time, time1, value1, time2, value2);
    }

    public static float getSpineBezierValue(float[] curves, float time, float time1, float value1, float time2,
                                            float value2) {
        if (curves == null || curves.length < BEZIER_SIZE) {
            throw new IllegalArgumentException("curves length must be at least " + BEZIER_SIZE);
        }

        if (time <= time1) return value1;
        if (time >= time2) return value2;

        int i = 0;
        if (curves[i] > time) {
            return lerp(value1, curves[i + 1], inverseLerp(time1, curves[i], time));
        }

        for (i += 2; i < BEZIER_SIZE; i += 2) {
            if (curves[i] >= time) {
                return lerp(curves[i - 1], curves[i + 1], inverseLerp(curves[i - 2], curves[i], time));
            }
        }

        float lastTime = curves[BEZIER_SIZE - 2];
        float lastValue = curves[BEZIER_SIZE - 1];
        return lerp(lastValue, value2, inverseLerp(lastTime, time2, time));
    }

    private static float inverseLerp(float start, float end, float value) {
        if (start == end) return 0;
        return (value - start) / (end - start);
    }

    private static float lerp(float start, float end, float alpha) {
        return start + (end - start) * alpha;
    }
}