package com.libGdx.test.spineanimation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class MoveTimeLine extends NewTemporalAction{
    private static final int X = 0;
    private static final int Y = 1;

    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float[] curvesX;
    private float[] curvesY;

    private float baseX;
    private float baseY;
    private float lastTime;
    public MoveTimeLine(){
        curvesX = new float[18];
        curvesY = new float[18];
    }

    @Override
    public void setTarget(Actor target) {
        super.setTarget(target);
    }

    @Override
    protected void update(float time) {
        float currentX = getBezierValue(curvesX, time, X);
        float currentY = getBezierValue(curvesY, time, Y);
        target.setPosition(baseX + currentX,baseY + currentY, Align.center);
        System.out.println(target.getX(Align.center)+"============"+target.getY(Align.center));
    }

    public void setCurvesX(float cx1, float cx2, float cx3, float cx4){
        setBezier(curvesX,lastTime,startX,cx1,cx2,cx3,cx4,getDuration(),endX);
    }

    public void setCurvesY(float cx1,float cx2,float cx3,float cx4){
        setBezier(curvesY,lastTime,startY,cx1,cx2,cx3,cx4,getDuration(),endY);
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
        if (time <= 0) return getStartValue(type);
        if (time >= getDuration()) {
            return getEndValue(type);
        }

        int i = 0;
        int n = 18;
        if (curves[i] > time) {
            float x = 0, y = getStartValue(type);
            return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
        }
        for (i += 2; i < n; i += 2) {
            if (curves[i] >= time) {
                float x = curves[i - 2], y = curves[i - 1];
                return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
            }
        }
        float lastValue = getEndValue(type);

        int length = curves.length;
        float ltime = curves[length - 1 - 1]; // 时间
        float lr = curves[length - 1]; //旋转角度
        if (getDuration() <= ltime) return lastValue;
        return  lr + (time - ltime) / (getDuration() - ltime) * (lastValue - lr);

    }

    private float getStartValue(int type) {
        return type == X ? startX : startY;
    }

    private float getEndValue(int type) {
        return type == X ? endX : endY;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public float[] getCurvesX() {
        return curvesX;
    }

    public void setCurvesX(float[] curvesX) {
        this.curvesX = curvesX;
    }

    public float[] getCurvesY() {
        return curvesY;
    }

    public void setCurvesY(float[] curvesY) {
        this.curvesY = curvesY;
    }

    public void setBaseX(float baseX) {
        this.baseX = baseX;
    }

    public void setBaseY(float baseY) {
        this.baseY = baseY;
    }

    public void setLastTime(float lastTime) {
        this.lastTime = lastTime;
    }
}
