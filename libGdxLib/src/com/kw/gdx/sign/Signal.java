package com.kw.gdx.sign;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Signal<T> {
    private HashMap<String,SignListener> listeners;
    public Signal(){
        this.listeners = new HashMap<>();
    }

    public void add( String key, SignListener<T> listener ) {
        if (!listeners.containsKey(key)){
            listeners.put(key,listener);
        }
    }

    public void removeAll(){
        listeners.clear();
    }

}
