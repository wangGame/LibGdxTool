package com.libGdx.test.ai.tetris.dqn;


import com.libGdx.test.ai.tetris.Tetris;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

public class TetrisEvn implements MDP<TetrisState, Integer, DiscreteSpace> {

    private Tetris tetris;

    private TetrisState tetrisState;

    private DiscreteSpace actionSpace = new DiscreteSpace(4);

    private ObservationSpace<TetrisState> observationSpace = new ArrayObservationSpace(new int[]{221});


    @Override
    public ObservationSpace<TetrisState> getObservationSpace() {
        return observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return actionSpace;
    }

    @Override
    public TetrisState reset() {
        if (tetris == null) {
            tetris = new Tetris();
        }
        tetris.start();
        return tetrisState = new TetrisState(0, tetris.toArray());
    }

    @Override
    public void close() {
        tetris.close();
    }

    @Override
    public StepReply<TetrisState> step(Integer integer) {
        moveStep(integer);
        tetrisState = new TetrisState(tetris.getScore(), tetris.toArray());
        return new StepReply<>(tetrisState, tetrisState.getScore(), isDone(), null);
    }

    private void moveStep(int action) {
        switch (action) {
            case 0: {
                tetris.left();
                break;
            }
            case 1: {
                tetris.right();
                break;
            }
            case 2: {
                tetris.up();
                break;
            }
            case 3: {
                tetris.dropDown();
                break;
            }
            default:{
                break;
            }
        }
    }

    @Override
    public boolean isDone() {
        return tetris.isOver();
    }

    @Override
    public MDP<TetrisState, Integer, DiscreteSpace> newInstance() {
        return new TetrisEvn();
    }


}
