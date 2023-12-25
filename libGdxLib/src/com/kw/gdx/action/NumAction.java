package com.kw.gdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.kw.gdx.resource.csvanddata.ConvertUtil;
import com.kw.gdx.utils.ClassType;

import java.lang.reflect.Field;

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
    private Double start, end;
    private Double value;

    /** Creates an IntAction that transitions from start to end. */
    public NumAction (Number start, Number end) {

        this.start = Double.valueOf(start.toString());
        this.end = Double.valueOf(end.toString());
    }

    protected void begin () {
        value = start;
    }

    protected void update (float percent) {
        value = (start + (end -start) * percent);
    }

    /** Gets the current int value. */
    public double getValue () {
        return value;
    }

    public void setStart(Double start) {
        this.start = start;
    }
}