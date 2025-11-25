package com.kw.gdx.fsm;

public interface State<T> {
    void enter(T entity);
    void exit(T entity);
    void update(T entity, float delta);
    void handleEvent(T entity, String event);
}