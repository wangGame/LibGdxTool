package com.libGdx.test.ray;

import com.badlogic.gdx.math.Vector2;

public class Ray {
    public static void findBy(Vector2 start,Vector2 step,float length){
        if (step.isZero()){
            return;
        }
        step.scl(2); //
        float bestLength = 10000;
        length *= length;

    }
}
