package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.kw.gdx.resource.csvanddata.ConvertUtil;
import com.kw.gdx.utils.ClassType;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * 有点翻车，初衷是使用泛型，让数字在一段时间内变化到目标值。
 *
 * 不过基本功能是存在的
 *
 * 1.s
 *
 * @Auther jian xian si qi
 * @Date 2023/12/25 9:53
 */
public class NumAction extends TemporalAction {
    private double start, end;
    private double value;
    private Runnable updateRunnable;
    private boolean isPause;
    public NumAction(){

    }
    /** Creates an IntAction that transitions from start to end. */
    public NumAction (Number start, Number end) {
        this.start = Double.valueOf(start.toString());
        this.end = Double.valueOf(end.toString());
        this.value = this.start;
    }

    public void setUpdateRunnable(Runnable updateRunnable) {
        this.updateRunnable = updateRunnable;
    }

    public Runnable getUpdateRunnable() {
        return updateRunnable;
    }

    protected void begin () {
        value = start;
    }

    protected void update (float percent) {
        value = start + (end - start) * percent;
        if (updateRunnable!=null) {
            updateRunnable.run();
        }
    }

    private Runnable endRunable;

    public void setEndRunable(Runnable endRunable) {
        this.endRunable = endRunable;
    }

    @Override
    protected void end() {
        super.end();
        value = end;
        if (endRunable!=null) {
            endRunable.run();
        }
    }

    /** Gets the current int value. */
    public double getValue () {
        return value;
    }

    public void setStart(double start) {
        this.start = start;
        this.value = start;
    }

    public void setEnd(double end) {
        this.end = end;
    }
}