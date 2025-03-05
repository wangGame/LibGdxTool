package com.kw.gdx.d3.utils;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

abstract class BaseShape implements Shape {
    protected final static Vector3 position = new Vector3();
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();

    public BaseShape(BoundingBox bounds) {
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
    }
}