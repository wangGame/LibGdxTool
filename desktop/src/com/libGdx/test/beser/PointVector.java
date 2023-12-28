package com.libGdx.test.beser;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/17 14:03
 */

public class PointVector {
    public int x, y;
    private  int  z = 0 ;

    public PointVector(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public PointVector(int x, int y , int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void init(int x,int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }
}
