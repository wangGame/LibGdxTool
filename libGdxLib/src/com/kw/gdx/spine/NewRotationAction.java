package com.kw.gdx.spine;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class NewRotationAction extends NewTemporalAction{
    private float startRotation;
    private float endRotation;
    private float[] curvesRotation;
    private float baseX;
    private float lastTime;
    public NewRotationAction(){
            curvesRotation = new float[18];
    }

    @Override
    public void setTarget(Actor target) {
        super.setTarget(target);
    }

    @Override
    protected void update(float time) {
        float currentX = getBezierValue(curvesRotation, time, 0);
        target.setRotation(baseX + (currentX - startRotation));
        System.out.println(time+"===================="+target.getRotation());
    }

    public void setCurvesRotation(float cx1, float cx2, float cx3, float cx4){
        setBezier(curvesRotation, startRotation,cx1,cx2,cx3,cx4,getDuration(), endRotation);
    }

    public void setBezier (float []curves,float value1, float cx11, float cy1, float cx22, float cy2, float time2, float value2) {
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
            float x = 0, y = startRotation;
            return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
        }
        for (i += 2; i < n; i += 2) {
            if (curves[i] >= time) {
                float x = curves[i - 2], y = curves[i - 1];
                return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
            }
        }
        int length = curves.length;
        float ltime = curves[length - 1 - 1]; // 时间
        float lr = curves[length - 1]; //旋转角度
        return  lr + (time - ltime) / (getDuration() - ltime) * (endRotation - lr);
    }

    public float getStartRotation() {
        return startRotation;
    }

    public void setStartRotation(float startRotation) {
        this.startRotation = startRotation;
    }

    public float getEndRotation() {
        return endRotation;
    }

    public void setEndRotation(float endRotation) {
        this.endRotation = endRotation;
    }

    public float[] getCurvesRotation() {
        return curvesRotation;
    }

    public void setCurvesRotation(float[] curvesRotation) {
        this.curvesRotation = curvesRotation;
    }

    public void setBaseX(float baseX) {
        this.baseX = baseX;
    }

    public void setLastTime(float lastTime) {
        this.lastTime = lastTime;
    }
}