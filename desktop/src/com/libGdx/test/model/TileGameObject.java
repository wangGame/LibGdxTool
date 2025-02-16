package com.libGdx.test.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.libGdx.test.model.g3.GameObject;

public class TileGameObject extends GameObject {
    private Vector3 vector3 = new Vector3();
    private float speed = 40;
    public TileGameObject() {
        super(ModelUtils.createInstance());
    }

    public void animation(){
        AnimationController animationController = new AnimationController(modelInstance);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//        vector3.set(vector3.x+delta*speed,vector3.y+delta*speed,vector3.z+delta*speed);
//        setRotation(vector3);
    }

    public static Vector3 transformDirection(Vector3 localDirection, Matrix4 transformationMatrix) {
        // 需要调用 transformDirection() 方法，而不是 simple transform()，以确保方向向量的长度不变
        Vector3 globalDirection = new Vector3(localDirection);
        transformDirection(transformationMatrix,globalDirection);
        return globalDirection;
    }

    public static Vector3 transformDirection(Matrix4 matrix, Vector3 direction) {
        // 将方向向量乘以矩阵
        Vector3 result = new Vector3(direction);  // 创建一个新的向量，防止修改原向量
        result.mul(matrix);  // 使用矩阵变换方向向量
        return result;
    }
}
