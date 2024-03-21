package com.libGdx.test.vect;

import com.badlogic.gdx.math.Vector2;

public class App {
    public static void main(String[] args) {
        Vector2 vector1 = new Vector2(2,2);
        Vector2 vector2 = new Vector2(2,2);
        float dot = vector1.dot(vector2);
        System.out.println(dot);
    }
}
