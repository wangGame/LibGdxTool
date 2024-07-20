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

    /** Creates an IntAction that transitions from start to end. */
    public NumAction (double start, double end) {
        this.start = start;
        this.end = end;
        value = start;
    }

    public void setUpdateRunnable(Runnable updateRunnable) {
        this.updateRunnable = updateRunnable;
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

    @Override
    protected void end() {
        super.end();
        value = end;
    }

    /** Gets the current int value. */
    public double getValue () {
        return value;
    }

    public void setStart(Double start) {
        this.start = start;
    }
}