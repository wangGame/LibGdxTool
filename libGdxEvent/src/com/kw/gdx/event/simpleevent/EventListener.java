package com.kw.gdx.event.simpleevent;

import java.util.ArrayList;

public abstract class EventListener<T> {
    protected ArrayList<SubTaskManager<T>> subTaskManagers = new ArrayList<>();
    public void listener(T t){}

    public void addSubTaskManagers(SubTaskManager<T> subTaskManager) {
        this.subTaskManagers.add(subTaskManager);
    }

    private ArrayList<SubTaskManager> finishSub = new ArrayList<>();

    public void update(float dt){
        for (SubTaskManager<T> subTaskManager : subTaskManagers) {
            if (subTaskManager.update(dt) && subTaskManager.condiction()){
                if (subTaskManager.getEventType() == EventType.Once) {
                    finishSub.add(subTaskManager);
                    listener(subTaskManager.getData());
                }else if (subTaskManager.getEventType() == EventType.CONDITION) {

                }
            }
        }
        for (SubTaskManager subTaskManager : finishSub) {
            subTaskManagers.remove(subTaskManager);
        }
        finishSub.clear();
    }
}
