package com.kw.gdx.spine;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class DmnActions extends Actions {

    static public MoveTimeLine newTemporalAction(
            float baseX,float baseY,
            float startX,float startY,float endX,float endY,
                                                 float cxx1,float cxx2,float cxx3,float cxx4,
                                                 float cxy1,float cxy2,float cxy3,float cxy4,
                                                 float time,  float lastTime
                                                 ){
        MoveTimeLine moveTimeLine = new MoveTimeLine();
        moveTimeLine.setLastTime(lastTime);
        moveTimeLine.setBaseX(baseX);
        moveTimeLine.setBaseY(baseY);
        moveTimeLine.setDuration(time);
        moveTimeLine.setStartX(startX);
        moveTimeLine.setStartY(startY);
        moveTimeLine.setEndX(endX);
        moveTimeLine.setEndY(endY);
        moveTimeLine.setCurvesX(cxx1,cxx2,cxx3,cxx4);
        moveTimeLine.setCurvesY(cxy1,cxy2,cxy3,cxy4);
        return moveTimeLine;
    }

    static public ScaleTemporalAction newScaleTemporalAction(
            float baseX,float baseY,
            float startX,float startY,float endX,float endY,
            float cxx1,float cxx2,float cxx3,float cxx4,
            float cxy1,float cxy2,float cxy3,float cxy4,
            float time, float lastTime
    ){
        ScaleTemporalAction action = new ScaleTemporalAction();
        action.setLastTime(lastTime);
        action.setBaseX(baseX);
        action.setBaseY(baseY);
        action.setDuration(time);
        action.setStartX(startX);
        action.setStartY(startY);
        action.setEndX(endX);
        action.setEndY(endY);
        action.setCurvesX(cxx1,cxx2,cxx3,cxx4);
        action.setCurvesY(cxy1,cxy2,cxy3,cxy4);
        return action;
    }

    static public NewAAction newAAction(
            float baseA,
            float startA,float endA,
            float cxx1,float cxx2,float cxx3,float cxx4,
            float time, float lastTime
    ){
        NewAAction action = new NewAAction();
        action.setLastTime(lastTime);
        action.setDuration(time);
        action.setBaseA(baseA);
        action.setStartA(startA);
        action.setEndA(endA);
        action.setCurvesA(cxx1,cxx2,cxx3,cxx4);
        return action;
    }

    static public NewRotationAction newRotationAction(
            float baseR,
            float startR,float endR,
            float cxx1,float cxx2,float cxx3,float cxx4,
            float time,float lastTime
    ){
        NewRotationAction rotationAction = new NewRotationAction();
        rotationAction.setDuration(time);
        rotationAction.setBaseX(baseR);
        rotationAction.setStartRotation(startR);
        rotationAction.setEndRotation(endR);
        rotationAction.setLastTime(lastTime);
        rotationAction.setCurvesRotation(cxx1,cxx2,cxx3,cxx4);
        return rotationAction;
    }
}
