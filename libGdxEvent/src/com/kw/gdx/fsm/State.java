package com.kw.gdx.fsm;

public abstract class State<T> {
    public State(){
        this.stateName = this.getClass().getSimpleName();
    }
    public String stateName;
    protected abstract void enter(T entity);
    protected abstract void exit(T entity);
    protected abstract void update(T entity, float delta);
    protected abstract void handleEvent(T entity, String event);
}