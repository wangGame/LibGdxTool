package com.kw.gdx.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class NewActions {
    static public <T extends Action> T action (Class<T> type) {
        Pool<T> pool = Pools.get(type);
        T action = pool.obtain();
        action.setPool(pool);
        return action;
    }

    static public MoveXAction moveXTo (float x, float duration, Interpolation interpolation) {
        MoveXAction action = action(MoveXAction.class);
        action.setEndX(x);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public MoveYAction moveYTo (float y, float duration, Interpolation interpolation) {
        MoveYAction action = action(MoveYAction.class);
        action.setY(y);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

}
