package com.libGdx.test.model;

import com.libGdx.test.model.g3.GameObject;

public class TileGameObject extends GameObject {
    public TileGameObject() {
        super(ModelUtils.createInstance(0));
    }

//
//    public static Vector3 transformDirection(Vector3 localDirection, Matrix4 transformationMatrix) {
//        // 需要调用 transformDirection() 方法，而不是 simple transform()，以确保方向向量的长度不变
//        Vector3 globalDirection = new Vector3(localDirection);
//        transformDirection(transformationMatrix,globalDirection);
//        return globalDirection;
//    }

//    public static Vector3 transformDirection(Matrix4 matrix, Vector3 direction) {
//        // 将方向向量乘以矩阵
//        Vector3 result = new Vector3(direction);  // 创建一个新的向量，防止修改原向量
//        result.mul(matrix);  // 使用矩阵变换方向向量
//        return result;
//    }
}
