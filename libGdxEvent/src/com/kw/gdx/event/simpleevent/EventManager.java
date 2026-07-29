package com.kw.gdx.event.simpleevent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private Map<String, ArrayList<EventListener>> delayEventListenerMap = new ConcurrentHashMap<>();

    public static EventManager getInstance() {
        return Singleton.getInstance(EventManager.class);
    }

    /**
     * 比如加金币  存在多个地方， 所以需要通过
     * @param name
     * @param eventListener
     * @param <T>
     */
    public <T> void addEventListener(String name,EventListener<T> eventListener){
        ArrayList<EventListener> eventListeners = this.delayEventListenerMap.get(name);
        if (eventListeners == null){
            eventListeners = new ArrayList<>();
            this.delayEventListenerMap.put(name,eventListeners);
        }
        if (!eventListeners.contains(eventListener)){
            eventListeners.add(eventListener);
        }
    }

    public <T> void submit(String name,T t) {
        ArrayList<EventListener> eventListeners = this.delayEventListenerMap.get(name);
        if (eventListeners!=null){
            for (EventListener eventListener : eventListeners) {
                eventListener.listener(t);
            }
        }
    }

    public <T> void submit(String keyName, SubTaskManager<T> subTaskManager) {
        ArrayList<EventListener> eventListeners = this.delayEventListenerMap.get(keyName);
        if (eventListeners!=null){
            for (EventListener eventListener : eventListeners) {
                eventListener.addSubTaskManagers(subTaskManager);
            }
        }
    }

    public void update(float dt){
        if (delayEventListenerMap.size()>0) {
            for (ArrayList<EventListener> value : delayEventListenerMap.values()) {
                for (EventListener delayEventListener : value) {
                    delayEventListener.update(dt);
                }
            }
        }
    }
}
