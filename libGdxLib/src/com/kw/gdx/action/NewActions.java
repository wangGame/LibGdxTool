package com.kw.gdx.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.kw.gdx.spine.SpineCurveMoveAction;

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


    static public SpineCurveMoveAction newSpineCurveMoveAction(
            float baseX,float baseY,
            float startX,float startY,float endX,float endY,
            float cxx1,float cxx2,float cxx3,float cxx4,
            float cxy1,float cxy2,float cxy3,float cxy4,
            float time, float lastTime
    ){
        SpineCurveMoveAction action = new SpineCurveMoveAction();
        action.setLastTime(lastTime);
        action.setBaseX(baseX);
        action.setBaseY(baseY);
        action.setDuration(time);
        action.setStartX(startX);
        action.setStartY(startY);
        action.setEndX(endX);
        action.setEndY(endY);
        action.setCurvesX(cxx1, cxx2, cxx3, cxx4);
        action.setCurvesY(cxy1, cxy2, cxy3, cxy4);
        return action;
    }

}
