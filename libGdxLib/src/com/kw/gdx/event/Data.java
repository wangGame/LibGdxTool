package com.kw.gdx.event;

public class Data {
    private String name;
    private String addr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}
