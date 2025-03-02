package com.kw.gdx.d3.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.Ray;

public interface Shape{
    boolean isVisible(Matrix4 transform, Camera cam);

    /**
     * @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection.
     */
    float intersects(Matrix4 transform, Ray ray);
}
