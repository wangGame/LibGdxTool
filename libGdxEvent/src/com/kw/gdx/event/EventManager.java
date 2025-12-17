package com.kw.gdx.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
    public static EventManager getInstance() {
        return Singleton.getInstance(EventManager.class);
    }

    private Map<String, ArrayList<EventListener>> eventListenerMap = new HashMap<>();

    public <T> void addEventListener(String name,EventListener<T> eventListener){
        ArrayList<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners == null){
            eventListeners = new ArrayList<>();
            this.eventListenerMap.put(name,eventListeners);
        }
        eventListeners.add(eventListener);
    }

    public void removeEventListener(String name,EventListener eventListener){
        ArrayList<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners == null){
            return;
        }
        eventListeners.remove(eventListener);
    }

    public <T> void onece(String name,T t){
        ArrayList<EventListener> removeList = this.eventListenerMap.remove(name);
        if (removeList!=null){
            for (EventListener eventListener : removeList) {
                eventListener.listener(t);
            }
        }
    }

    public <T> void sumbit(String name,T t){
        ArrayList<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners!=null){
            for (EventListener eventListener : eventListeners) {
                eventListener.listener(t);
            }
        }
    }
}
