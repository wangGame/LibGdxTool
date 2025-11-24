package com.kw.gdx.singleton;

public class SingletonB extends Singleton{
    public static SingletonB getInstance() {
        return Singleton.getInstance(SingletonB.class);
    }
}
