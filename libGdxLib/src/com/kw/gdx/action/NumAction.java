package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Pool;

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

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isPause() {
        return isPause;
    }

    private Runnable endRunable;

    public void setEndRunable(Runnable endRunable) {
        this.endRunable = endRunable;
    }

    @Override
    protected void end() {
        super.end();
        value = end;

        if (updateRunnable!=null){
            updateRunnable.run();
        }
        if (endRunable!=null) {
            endRunable.run();
        }
        updateRunnable = null;
        endRunable = null;
    }

    /**
     * Gets the current int value.
     */
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


    public boolean act (float delta) {
        if (complete) return true;
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executing.
        try {
            if (!began) {
                begin();
                began = true;
            }
            if (!isPause) {
                time += delta;
            }
            complete = time >= duration;
            float percent = complete ? 1 : time / duration;
            if (interpolation != null) percent = interpolation.apply(percent);
            update(reverse ? 1 - percent : percent);
            if (complete) end();
            return complete;
        } finally {
            setPool(pool);
        }
    }
}