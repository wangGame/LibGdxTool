package com.libGdx.test.ai.labyrinth;

import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;

public class PathState implements Encodable {
    private int x;
    private int y;
    private int state;
    public static int MATRIX_LENGTH = Constant.size;

    public PathState(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = MATRIX_LENGTH*y+x;
    }

    public static int calState(int x,int y){
        return MATRIX_LENGTH*y+x;
    }

    public boolean isEqual(PathState koordinat){
        if((this.x == koordinat.getX())&&(this.y == koordinat.getY())){
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "("+x+","+y+","+"K"+")\n";
    }

    @Override
    public double[] toArray() {
        return new double[]{state};
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public INDArray getData() {
        return null;
    }

    @Override
    public Encodable dup() {
        return null;
    }
}
