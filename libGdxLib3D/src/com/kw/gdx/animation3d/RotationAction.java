package com.kw.gdx.animation3d;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.ModelActor;
import com.kw.gdx.ModelGroup;

public class RotationAction  extends TemporalAction {
    private float startX, startY,startZ;
    private float endX, endY,endZ;


    protected void begin () {
        if (target instanceof ModelGroup) {
            ModelGroup target1 = (ModelGroup) (target);
            Vector3 lrot = target1.get_lrot();
            startX = lrot.x;
            startY = lrot.y;
            startZ = lrot.z;
        }
    }

    protected void update (float percent) {
        if (target instanceof ModelActor) {
            ((ModelActor) target).rotation(startX + (endX - startX) * percent, startY + (endY - startY) * percent, startZ + (endZ - startZ) * percent);
        }
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getStartZ() {
        return startZ;
    }

    public void setStartZ(float startZ) {
        this.startZ = startZ;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public float getEndZ() {
        return endZ;
    }

    public void setEndZ(float endZ) {
        this.endZ = endZ;
    }
}
