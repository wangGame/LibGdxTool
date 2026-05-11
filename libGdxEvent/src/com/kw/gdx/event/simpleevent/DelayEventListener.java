package com.kw.gdx.event.simpleevent;

import java.util.ArrayList;


public class DelayEventListener<T> extends EventListener<T>{
    private ArrayList<SubTaskManager<T>> subTaskManagers = new ArrayList<>();
    private ArrayList<SubTaskManager> finishSub = new ArrayList<>();
    public DelayEventListener(){
        setEventType(EventType.Once);
    }

    public void update(float dt){
        for (SubTaskManager<T> subTaskManager : subTaskManagers) {
            if (subTaskManager.update(dt)){
                listener(subTaskManager.getData());
                finishSub.add(subTaskManager);
            }
        }
        for (SubTaskManager subTaskManager : finishSub) {
            subTaskManagers.remove(subTaskManager);
        }
    }

    public void addSubTaskManagers(SubTaskManager<T> subTaskManager) {
        this.subTaskManagers.add(subTaskManager);
    }
}
