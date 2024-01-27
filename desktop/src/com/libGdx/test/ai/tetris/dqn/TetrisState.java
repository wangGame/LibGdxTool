package com.libGdx.test.ai.tetris.dqn;

import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;

public class TetrisState implements Encodable {

    private double score;

    private double[] data;

    public double getScore() {
        return score;
    }

    public TetrisState(double score, double[] data) {
        this.score =  score;
        this.data  =  data;
    }

    @Override
    public double[] toArray() {
        return data;
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
