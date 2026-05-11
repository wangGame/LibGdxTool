package com.kw.gdx.event.simpleevent;

public abstract class EventListener<T> {
    private EventType eventType = EventType.Once; //立即

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void listener(T t){}
    public void listener(){}
}
