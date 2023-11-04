package com.libGdx.test.path;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

/**
 * @Auther jian xian si qi
 * @Date 2023/8/30 19:03
 */
public class PathAnimation extends TemporalAction {
    private Array<Vector2> pos;
    private Vector2 basePos;
    public PathAnimation(){
        pos = new Array<>();
    }

    public void setPosData(double[] data,float start , float ap){
        if(pos!=null){
            pos.clear();
            pos = null;
        }
        int index = 0;
        pos = new Array<>();
//        for (int i = (int) ((data.length-1)*start); i < (data.length-1)*ap/2; i++) {
//            Vector2 vector2 = new Vector2();
//            vector2.set((float) data[index++],(float) data[index++]);
//            pos.add(vector2);
//        }
        for (int i = (int) ((data.length-1)*start); i < (data.length-1)/2; i++) {
            Vector2 vector2 = new Vector2();
            vector2.set((float) data[index++],(float) data[index++]);
            pos.add(vector2);
        }
    }

    @Override
    protected void update(float percent) {
        if (basePos==null){
            basePos = new Vector2();
            basePos.set(target.getX(),target.getY());
        }
        Vector2 vector2 = pos.get((int) (percent*(pos.size-1)));
        target.setPosition(vector2.x+basePos.x, vector2.y+basePos.y);
    }
}