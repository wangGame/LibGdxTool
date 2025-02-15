package com.libGdx.test.dfs;

public class IndexBean {
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void addNum(){
        index ++;
    }

    public void minsNum(){
        index -- ;
    }
}
