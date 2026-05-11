package com.kw.gdx.event.simpleevent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private Map<String, ArrayList<EventListener>> eventListenerMap = new ConcurrentHashMap<>();
    private Map<String, ArrayList<DelayEventListener>> delayEventListenerMap = new ConcurrentHashMap<>();

    public static EventManager getInstance() {
        return Singleton.getInstance(EventManager.class);
    }

    public <T> void addEventListener(String name,EventListener<T> eventListener){
        if (eventListener instanceof DelayEventListener){
            ArrayList<DelayEventListener> eventListeners = this.delayEventListenerMap.get(name);
            if (eventListeners == null){
                eventListeners = new ArrayList<>();
                this.delayEventListenerMap.put(name,eventListeners);
            }
            if (!eventListeners.contains(eventListener)){
                eventListeners.add((DelayEventListener) eventListener);
            }
        }else {
            ArrayList<EventListener> eventListeners = this.eventListenerMap.get(name);
            if (eventListeners == null){
                eventListeners = new ArrayList<>();
                this.eventListenerMap.put(name,eventListeners);
            }
            if (!eventListeners.contains(eventListener)){
                eventListeners.add(eventListener);
            }
        }
    }

    public <T> void removeEventListener(String name,EventListener<T> eventListener){
        ArrayList<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners == null){
            return;
        }
        eventListeners.remove(eventListener);
        if (eventListeners.size()<=0){
            this.eventListenerMap.remove(name);
        }
    }

    public <T> void once(String name,T t){
        ArrayList<EventListener> removeList = this.eventListenerMap.remove(name);
        if (removeList!=null){
            for (EventListener eventListener : removeList) {
                eventListener.listener(t);
            }
        }
    }

    public <T> void delayOnce(String name,T t){
        ArrayList<EventListener> removeList = this.eventListenerMap.remove(name);
        if (removeList!=null){
            for (EventListener eventListener : removeList) {
                eventListener.listener(t);
            }
        }
    }

    public <T> void submit(String name,T t,float time){
        ArrayList<DelayEventListener> eventListeners = this.delayEventListenerMap.get(name);
        if (eventListeners!=null){
            for (DelayEventListener eventListener : eventListeners) {
                SubTaskManager subTaskManager = new SubTaskManager();
                subTaskManager.setTime(time);
                subTaskManager.setData(t);
                eventListener.addSubTaskManagers(subTaskManager);
            }
        }
    }

    public <T> void submit(String name,T t){
        ArrayList<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners!=null){
            for (EventListener eventListener : eventListeners) {
                eventListener.listener(t);
            }
        }
    }

    public void update(float dt){
        if (delayEventListenerMap.size()>0) {
            for (ArrayList<DelayEventListener> value : delayEventListenerMap.values()) {
                for (DelayEventListener delayEventListener : value) {
                    delayEventListener.update(dt);
                }
            }
        }
    }

}
