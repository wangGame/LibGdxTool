package com.kw.gdx.resource.csvanddata.demo;

public class RiderChapter {
    private int chap;
    private int star;

    public int getChap() {
        return chap;
    }

    public void setChap(int chap) {
        this.chap = chap;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "RiderChapter{" +
                "chap=" + chap +
                ", star=" + star +
                '}';
    }
}

