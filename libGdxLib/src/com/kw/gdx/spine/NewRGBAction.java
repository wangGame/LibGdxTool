package com.kw.gdx.spine;

import com.badlogic.gdx.graphics.Color;

public class NewRGBAction extends NewTemporalAction{
    private float startR;
    private float startG;
    private float startB;
    private float startA;
    private float endR;
    private float endG;
    private float endB;
    private float endA;
    private float[] curvesR;
    private float[] curvesG;
    private float[] curvesB;
    private float[] curvesA;
    private float baseR;
    private float baseG;
    private float baseB;
    private float baseA;
    private float lastTime;
    public NewRGBAction(){
        curvesR = new float[18];
        curvesG = new float[18];
        curvesB = new float[18];
        curvesA = new float[18];
    }

    @Override
    protected void update(float time) {
        float currentR = getBezierValue(curvesR, time, 0);
        float currentG = getBezierValue(curvesG, time, 1);
        float currentB = getBezierValue(curvesR, time, 2);
        float currentA = getBezierValue(curvesA, time, 3);
        Color color = target.getColor();
        color.r = currentR;
        color.g = currentG;
        color.b = currentB;
        color.a = currentA;
    }

    public void setCurvesR(float cx1, float cx2, float cx3, float cx4){
        setBezier(curvesR,lastTime,startR,cx1,cx2,cx3,cx4,getDuration(),endR);
    }

    public void setCurvesG(float cx1,float cx2,float cx3,float cx4){
        setBezier(curvesG,lastTime,startG,cx1,cx2,cx3,cx4,getDuration(),endG);
    }

    public void setCurvesB(float cx1, float cx2, float cx3, float cx4){
        setBezier(curvesB,lastTime,startB,cx1,cx2,cx3,cx4,getDuration(),endB);
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
            float x = 0, y = startR;
            if (type == 0){
                y = startR;
            }else if (type == 1){
                y = startG;
            }else if (type == 2){
                y = startB;
            }else if (type == 3){
                y = startA;
            }
            return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
        }
        for (i += 2; i < n; i += 2) {
            if (curves[i] >= time) {
                float x = curves[i - 2], y = curves[i - 1];
                return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
            }
        }

        float lastValue = 0;
        if (type == 0){
            lastValue = endR;
        }else if (type == 1){
            lastValue = endG;
        }else if (type == 2){
            lastValue = endB;
        }else if (type == 3){
            lastValue = endA;
        }


        int length = curves.length;
        float ltime = curves[length - 1 - 1]; // 时间
        float lr = curves[length - 1]; //旋转角度
        return  lr + (time - ltime) / (getDuration() - ltime) * (lastValue - lr);

    }

    public float getStartR() {
        return startR;
    }

    public void setStartR(float startR) {
        this.startR = startR;
    }

    public float getStartG() {
        return startG;
    }

    public void setStartG(float startG) {
        this.startG = startG;
    }

    public float getStartB() {
        return startB;
    }

    public void setStartB(float startB) {
        this.startB = startB;
    }

    public float getStartA() {
        return startA;
    }

    public void setStartA(float startA) {
        this.startA = startA;
    }

    public float getEndR() {
        return endR;
    }

    public void setEndR(float endR) {
        this.endR = endR;
    }

    public float getEndG() {
        return endG;
    }

    public void setEndG(float endG) {
        this.endG = endG;
    }

    public float getEndB() {
        return endB;
    }

    public void setEndB(float endB) {
        this.endB = endB;
    }

    public float getEndA() {
        return endA;
    }

    public void setEndA(float endA) {
        this.endA = endA;
    }

    public float getBaseR() {
        return baseR;
    }

    public void setBaseR(float baseR) {
        this.baseR = baseR;
    }

    public float getBaseG() {
        return baseG;
    }

    public void setBaseG(float baseG) {
        this.baseG = baseG;
    }

    public float getBaseB() {
        return baseB;
    }

    public void setBaseB(float baseB) {
        this.baseB = baseB;
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
