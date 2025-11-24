package com.kw.gdx.event;

import com.badlogic.gdx.utils.Array;
import com.kw.gdx.singleton.Singleton;

import java.util.HashMap;
import java.util.Map;

public class EventManager {
    public static EventManager getInstance() {
        return Singleton.getInstance(EventManager.class);
    }

    private Map<String, Array<EventListener>> eventListenerMap = new HashMap<>();

    public <T> void addEventListener(String name,EventListener<T> eventListener){
        Array<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners == null){
            eventListeners = new Array<>();
            this.eventListenerMap.put(name,eventListeners);
        }
        eventListeners.add(eventListener);
    }

    public void removeEventListener(String name,EventListener eventListener){
        Array<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners == null){
            return;
        }
        eventListeners.removeValue(eventListener,false);
    }

    public <T> void sumbit(String name,T t){
        Array<EventListener> eventListeners = this.eventListenerMap.get(name);
        if (eventListeners!=null){
            for (EventListener eventListener : eventListeners) {
                eventListener.listener(t);
            }
        }
    }
}
