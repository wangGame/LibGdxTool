package com.kw.gdx.event.simpleevent;

public class SubTaskManager<T> {
    public float time = 0;
    private T data;

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
}
