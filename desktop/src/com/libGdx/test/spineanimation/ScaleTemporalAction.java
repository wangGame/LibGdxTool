package com.libGdx.test.spineanimation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class ScaleTemporalAction extends NewTemporalAction {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float[] curvesX;
    private float[] curvesY;

    private float baseX;
    private float baseY;
    public ScaleTemporalAction(){
        curvesX = new float[18];
        curvesY = new float[18];
    }

    @Override
    public void setTarget(Actor target) {
        super.setTarget(target);
    }

    @Override
    protected void update(float time) {
        float currentX = getBezierValue(curvesX, time, 0);
        float currentY = getBezierValue(curvesY, time, 1);

        target.setPosition(baseX + currentX,baseY + currentY, Align.center);
        System.out.println(target.getX(Align.center) + "     "+ target.getY(Align.center));
    }

    public void setCurvesX(float cx1, float cx2, float cx3, float cx4){
        setBezier(curvesX,0,cx1,cx2,cx3,cx4,getDuration(),endX);
    }

    public void setCurvesY(float cx1,float cx2,float cx3,float cx4){
        setBezier(curvesY,1512,cx1,cx2,cx3,cx4,getDuration(),endY);
    }

    public void setBezier (float []curves,float value1, float cx1, float cy1, float cx2, float cy2, float time2, float value2) {
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
            float x = 0, y = startY;
            if (type == 0){
                y = startX;
            }
            return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
        }
        for (i += 2; i < n; i += 2) {
            if (curves[i] >= time) {
                float x = curves[i - 2], y = curves[i - 1];
                return y + (time - x) / (curves[i] - x) * (curves[i + 1] - y);
            }
        }
        if (type == 0){
            return endX;
        }else if (type == 1){
            return endY;
        }
        return 0;
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
}