package com.libGdx.test.spineanimation;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;


import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;

public abstract class NewTemporalAction extends Action {
    private float duration, time;
    private Interpolation interpolation;
    private boolean reverse, began, complete;

    public NewTemporalAction () {
    }

    public NewTemporalAction (float duration) {
        this.duration = duration;
    }

    public NewTemporalAction (float duration, Interpolation interpolation) {
        this.duration = duration;
        this.interpolation = interpolation;
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
            time += delta;
            complete = time >= duration;
            if (complete){
                time = duration;
            }
            float appliedTime = time;
            if (duration > 0) {
                float percent = time / duration;
                if (reverse) percent = 1 - percent;
                if (interpolation != null) percent = interpolation.apply(percent);
                appliedTime = percent * duration;
            }
            update(appliedTime);
            if (complete) end();
            return complete;
        } finally {
            setPool(pool);
        }
    }

    public float getBasePercent(){
        if (duration <= 0) return 1;
        return time / duration;
    }

    /** Called the first time {@link #act(float)} is called. This is a good place to query the {@link #actor actor's} starting
     * state. */
    protected void begin () {
    }

    /** Called the last time {@link #act(float)} is called. */
    protected void end () {
    }

    /** Called each frame.
     * @param time The interpolated local time for this action, growing from 0 to {@link #getDuration()}. If
     *           {@link #setReverse(boolean) reversed}, this will shrink from the duration to 0. */
    abstract protected void update (float time);

    /** Skips to the end of the transition. */
    public void finish () {
        time = duration;
    }

    public void restart () {
        time = 0;
        began = false;
        complete = false;
    }

    public void reset () {
        super.reset();
        time = 0;
        began = false;
        complete = false;
        reverse = false;
        interpolation = null;
    }

    /** Gets the transition time so far. */
    public float getTime () {
        return time;
    }

    /** Sets the transition time so far. */
    public void setTime (float time) {
        this.time = time;
    }

    public float getDuration () {
        return duration;
    }

    /** Sets the length of the transition in seconds. */
    public void setDuration (float duration) {
        this.duration = duration;
    }

    public Interpolation getInterpolation () {
        return interpolation;
    }

    public void setInterpolation (Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public boolean isReverse () {
        return reverse;
    }

    /** When true, the action's progress will go from 100% to 0%. */
    public void setReverse (boolean reverse) {
        this.reverse = reverse;
    }
}
