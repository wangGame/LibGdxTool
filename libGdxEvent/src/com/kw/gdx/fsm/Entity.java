package com.kw.gdx.fsm;

import com.badlogic.gdx.utils.ArrayMap;

public abstract class Entity<T extends Entity> {

    public ArrayMap<String,State<T>> stateArrayMap;
    public StateMachine<T> stateMachine;

    public abstract void update(float delta);
    public abstract void handleEvent(String event);
}
