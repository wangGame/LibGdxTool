package com.kw.gdx.fsm;

public interface State<T> {
    void onEnter(T owner, Object... args);
    void onUpdate(T owner, float delta);
    void onExit(T owner);
}