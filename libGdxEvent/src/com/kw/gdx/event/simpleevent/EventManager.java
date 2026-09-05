package com.kw.gdx.event.simpleevent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 目前事件包括延迟和单词，如果有需要在进行添加
 *
 * 还不支持跨screen的事件， 目前只支持单个screen的事件
 *
 * 有需要在添加
 *
 * TODO 下来要添加单个事件   和   多个事件
 *
 * 也就是eventName 只对应一个listener  和  eventName 对应多个listener
 */
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
