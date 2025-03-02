package com.kw.gdx.d3.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class Box extends BaseShape {
    public Box(BoundingBox bounds) {
        super(bounds);
    }

    @Override
    public boolean isVisible(Matrix4 transform, Camera cam) {
        return cam.frustum.boundsInFrustum(transform.getTranslation(position).add(center), dimensions);
    }

    @Override
    public float intersects(Matrix4 transform, Ray ray) {
        transform.getTranslation(position).add(center);
        if (Intersector.intersectRayBoundsFast(ray, position, dimensions))
            return ray.origin.dst2(position);
        return -1f;
    }
}