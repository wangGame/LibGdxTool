package com.kw.gdx.spine;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.besier.SpineCurveUtils;


public class SpineCurveMoveAction extends NewTemporalAction {
    private static final int X = 0;
    private static final int Y = 1;

    private final float[] curvesX = new float[SpineCurveUtils.BEZIER_SIZE];
    private final float[] curvesY = new float[SpineCurveUtils.BEZIER_SIZE];

    private float baseX;
    private float baseY;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float lastTime;

    @Override
    public void setTarget(Actor target) {
        super.setTarget(target);
    }

    @Override
    protected void update(float time) {
        float currentX = SpineCurveUtils.getSpineBezierValue(curvesX, time, 0, startX, getDuration(), endX);
        float currentY = SpineCurveUtils.getSpineBezierValue(curvesY, time, 0, startY, getDuration(), endY);
        target.setPosition(baseX + currentX, baseY + currentY, Align.center);
    }

    public void setCurvesX(float cx1, float cy1, float cx2, float cy2) {
        SpineCurveUtils.setSpineBezier(curvesX, 0, startX, cx1 - lastTime, cy1, cx2 - lastTime, cy2, getDuration(), endX);
    }

    public void setCurvesY(float cx1, float cy1, float cx2, float cy2) {
        SpineCurveUtils.setSpineBezier(curvesY, 0, startY, cx1 - lastTime, cy1, cx2 - lastTime, cy2, getDuration(), endY);
    }

    public void setBaseX(float baseX) {
        this.baseX = baseX;
    }

    public void setBaseY(float baseY) {
        this.baseY = baseY;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public void setLastTime(float lastTime) {
        this.lastTime = lastTime;
    }
}