package com.kw.gdx.event.simpleevent;

import java.util.ArrayList;

public abstract class EventListener<T> {
    protected ArrayList<SubTaskManager<T>> subTaskManagers = new ArrayList<>();
    public void listener(T t){}

    public void addSubTaskManagers(SubTaskManager<T> subTaskManager) {
        this.subTaskManagers.add(subTaskManager);
    }

    public void update(float dt){

    }
}
