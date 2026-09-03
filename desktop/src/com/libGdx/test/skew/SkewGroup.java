package com.libGdx.test.skew;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 *  skew group, allows for skewing of its children actors.
 *
 */
public class SkewGroup extends Group {
    private float skewX = 0;
    private float skewY = 0;
    private final Matrix4 oldTransform = new Matrix4();
    private final Matrix4 transform = new Matrix4();
    private final Matrix4 skewMatrix = new Matrix4();

    public SkewGroup() {
        setTransform(true);
    }

    public void setSkew(float skewX, float skewY) {
        this.skewX = skewX;
        this.skewY = skewY;
    }

    public void setSkewX(float skewX) {
        this.skewX = skewX;
    }

    public void setSkewY(float skewY) {
        this.skewY = skewY;
    }

    public float getSkewX() {
        return skewX;
    }

    public float getSkewY() {
        return skewY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        oldTransform.set(batch.getTransformMatrix());
        float originX = getOriginX();
        float originY = getOriginY();
        transform.set(oldTransform);
        transform.translate(getX() + originX, getY() + originY, 0);
        if (getRotation() != 0) {
            transform.rotate(0, 0, 1, getRotation());
        }
        if (getScaleX() != 1 || getScaleY() != 1) {
            transform.scale(getScaleX(), getScaleY(), 1);
        }
        float tanX = (float) Math.tan(Math.toRadians(skewX));
        float tanY = (float) Math.tan(Math.toRadians(skewY));
        skewMatrix.idt();
        skewMatrix.val[Matrix4.M01] = tanX;
        skewMatrix.val[Matrix4.M10] = tanY;
        transform.mul(skewMatrix);
        transform.translate(-originX, -originY, 0);
        batch.flush();
        batch.setTransformMatrix(transform);
        drawChildren(batch, parentAlpha);
        batch.flush();
        batch.setTransformMatrix(oldTransform);
    }
}