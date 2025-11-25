package com.kw.gdx.fsm;
public abstract class Entity<T> {
    public StateMachine<T> stateMachine;

    public abstract void update(float delta);
    public abstract void handleEvent(String event);
}
