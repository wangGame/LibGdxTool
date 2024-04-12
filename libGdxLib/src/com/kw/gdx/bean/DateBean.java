package com.kw.gdx.bean;

public class DateBean {
    private float hour;
    private float minute;
    private float second;

    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public float getMinute() {
        return minute;
    }

    public void setMinute(float minute) {
        this.minute = minute;
    }

    public float getSecond() {
        return second;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "DateBean{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }
}
