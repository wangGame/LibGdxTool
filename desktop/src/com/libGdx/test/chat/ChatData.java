package com.libGdx.test.chat;

public class ChatData {
    private int id;
    private double value;

    @Override
    public String toString() {
        return "ChatData{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
