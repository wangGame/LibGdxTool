package com.kw.gdx.singleton;

public class SingletonA extends Singleton{
    public static SingletonA getInstance() {
        return Singleton.getInstance(SingletonA.class);
    }
}
