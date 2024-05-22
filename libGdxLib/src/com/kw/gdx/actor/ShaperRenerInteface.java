package com.kw.gdx.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public interface ShaperRenerInteface {
    public void draw(Batch batch,float a);

    void setProjectionMatrix(Matrix4 projectionMatrix);

    void setTransformMatrix(Matrix4 transformMatrix);

    void begin(ShapeRenderer.ShapeType filled);

    void setColor(Color color);

    void end();
}
