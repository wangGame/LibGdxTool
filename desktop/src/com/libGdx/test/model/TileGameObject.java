package com.libGdx.test.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.libGdx.test.model.g3.GameObject;

public class TileGameObject extends GameObject {
    private Vector3 vector3 = new Vector3();
    private float speed = 40;
    public TileGameObject() {
        super(ModelUtils.createInstance());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        vector3.set(vector3.x+delta*speed,vector3.y+delta*speed,vector3.z+delta*speed);
        setRotation(vector3);
    }
}
