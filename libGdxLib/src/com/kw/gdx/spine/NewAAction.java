package com.kw.gdx.spine;


import com.badlogic.gdx.graphics.Color;

public class NewAAction extends NewTemporalAction{
    private float startA;
    private float endA;
    private float[] curvesA;
    private float baseA;
    private float lastTime;
    public NewAAction(){
        curvesA = new float[18];
    }

    @Override
    protected void update(float time) {
        float currentA = getBezierValue(curvesA, time, 3);
        Color color = target.getColor();
        color.a = currentA;
    }

    public void setCurvesA(float cx1,float cx2,float cx3,float cx4){
        setBezier(curvesA,lastTime,startA,cx1,cx2,cx3,cx4,getDuration(),endA);
    }

    public void setBezier (float []curves,float lastTime,float value1, float cx11, float cy1, float cx22, float cy2, float time2, float value2) {
        float cx1 = cx11 - lastTime;
        float cx2 = cx22 - lastTime;
        int i = 0;
        float tmpx = (- cx1 * 2 + cx2) * 0.03f, tmpy = (value1 - cy1 * 2 + cy2) * 0.03f;
        float dddx = ((cx1 - cx2) * 3 + time2) * 0.006f, dddy = ((cy1 - cy2) * 3 - value1 + value2) * 0.006f;
        float ddx = tmpx * 2 + dddx, ddy = tmpy * 2 + dddy;
        float dx = (cx1) * 0.3f + tmpx + dddx * 0.16666667f, dy = (cy1 - value1) * 0.3f + tmpy + dddy * 0.16666667f;
        float x = + dx, y = value1 + dy;
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

    public float getBezierValue (float[] curves,float time,int type) {
        int i = 0;
        int n = 18;
        if (curves[i] > time) {
            float x = 0, y = startA;
            return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
        }
        for (i += 2; i < n; i += 2) {
            if (curves[i] >= time) {
                float x = curves[i - 2], y = curves[i - 1];
                return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
            }
        }
        float lastValue = endA;
        int length = curves.length;
        float ltime = curves[length - 1 - 1]; // 时间
        float lr = curves[length - 1]; //旋转角度
        return  lr + (time - ltime) / (getDuration() - ltime) * (lastValue - lr);
    }

    public float getStartA() {
        return startA;
    }

    public void setStartA(float startA) {
        this.startA = startA;
    }

    public float getEndA() {
        return endA;
    }

    public void setEndA(float endA) {
        this.endA = endA;
    }

    public float getBaseA() {
        return baseA;
    }

    public void setBaseA(float baseA) {
        this.baseA = baseA;
    }

    public float getLastTime() {
        return lastTime;
    }

    public void setLastTime(float lastTime) {
        this.lastTime = lastTime;
    }
}
