package com.kw.gdx.resource.csvanddata;

public class RiderBonus {
    private int chap;
    private String star;

    public void setChap(int chap) {
        this.chap = chap;
    }

    public int getChap() {
        return chap;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getStar() {
        return star;
    }

    // TODO: 2022/5/25

    @Override
    public String toString() {
        return "RiderBonus{" +
                "chap=" + chap +
                ", star='" + star + '\'' +
                '}';
    }
}
