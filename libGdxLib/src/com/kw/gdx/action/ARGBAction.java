package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ARGBAction extends TemporalAction {
    private float startA, startR, startG, startB;
    private float endA, endR, endG, endB;

    public float getStartA() {
        return startA;
    }

    public void setStartA(float startA) {
        this.startA = startA;
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

    public float getEndA() {
        return endA;
    }

    public void setEndA(float endA) {
        this.endA = endA;
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

    @Override
    protected void update(float percent) {
        float a = startA + (endA - startA) * percent;
        float r = startR + (endR - startR) * percent;
        float g = startG + (endG - startG) * percent;
        float b = startB + (endB - startB) * percent;
        target.setColor(r, g, b, a);
    }
}
