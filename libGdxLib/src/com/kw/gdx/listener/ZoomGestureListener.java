package com.kw.gdx.listener;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * 使用：
 *
 *   zoomGestureListener = new ZoomGestureListener(this);
 *   this.addListener(zoomGestureListener);
 */
public class ZoomGestureListener extends ActorGestureListener {
    private Actor targetActor;
    private boolean start;
    private boolean reseting;

    public ZoomGestureListener(Actor targetActor) {
        this.targetActor = targetActor;
    }

    @Override
    public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        if (reseting)return;
        //只设置一次缩放中心
        if (start) {
            float vx = (initialPointer1.x + initialPointer2.x) / 2f;
            float vy = (initialPointer1.y + initialPointer2.y) / 2f;
            start = false;
            targetActor.setOrigin(vx,vy);
        }
        float dst = initialPointer1.dst(initialPointer2);
        float dst1 = pointer1.dst(pointer2);
        float v;
        v = dst1 / dst;
        if (v>=1){
            targetActor.setScale(1 + (v-1) * 0.3F);
        }
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        start = true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        start = false;
        targetActor.addAction(
                Actions.sequence(
                        Actions.scaleTo(1,1,0.2f),
                        Actions.run(()->{
                            reseting = false;
                        })));
    }

}
