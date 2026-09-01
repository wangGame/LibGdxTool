package com.kw.gdx.event.simpleevent;

public class SubTaskManager<T> {
    private float time = 0;
    private T data;
    private EventType eventType = EventType.Once;

    public void setTime(float time) {
        this.time = time;
    }

    public void setData(T data) {
        this.data = data;
    }

    public float getTime() {
        return time;
    }

    public boolean update(float dt){
        time -= dt;
        return time<=0;
    }

    public T getData() {
        return data;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    /**
     * 这里有两种方式处理：一种复写， 一种是调用一个方法  返回一个bool值
     */
    public boolean condiction(){
        return true;
    }
}
