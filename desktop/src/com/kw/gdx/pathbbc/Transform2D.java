package com.kw.gdx.pathbbc;

import com.badlogic.gdx.math.Vector2;

/**
 * 附件自身的 2D 变换。
 *
 * freezeRotationScale 会把 rotation/scale/shear 烘焙进顶点，
 * 然后把 rotation/shear 归零、scale 归 1。
 */
public class Transform2D {
    public float x;
    public float y;
    public float rotation;
    public float scaleX = 1f;
    public float scaleY = 1f;
    public float shearX;
    public float shearY;

    private final Vector2 tmp = new Vector2();

    public Vector2 apply(float localX, float localY) {
        float sx = localX * scaleX;
        float sy = localY * scaleY;

        if (shearX != 0f || shearY != 0f) {
            float tx = sx + sy * shearX;
            float ty = sy + sx * shearY;
            sx = tx;
            sy = ty;
        }

        if (rotation != 0f) {
            float cos = (float)Math.cos(Math.toRadians(rotation));
            float sin = (float)Math.sin(Math.toRadians(rotation));
            float rx = sx * cos - sy * sin;
            float ry = sx * sin + sy * cos;
            sx = rx;
            sy = ry;
        }

        return tmp.set(sx + x, sy + y);
    }

    /**
     * 只重置旋转、缩放、斜切。位置不重置。
     */
    public void resetRotationScaleShear() {
        rotation = 0f;
        scaleX = 1f;
        scaleY = 1f;
        shearX = 0f;
        shearY = 0f;
    }
}
