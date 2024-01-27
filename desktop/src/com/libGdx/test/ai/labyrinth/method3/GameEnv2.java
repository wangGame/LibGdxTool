package com.libGdx.test.ai.labyrinth.method3;



import com.libGdx.test.ai.labyrinth.BoardGroup;
import com.libGdx.test.ai.labyrinth.Constant;
import com.libGdx.test.ai.labyrinth.PathState;
import com.libGdx.test.ai.tetris.dqn.TetrisState;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

import java.util.Random;

public class GameEnv2 implements MDP<PathState, Integer, DiscreteSpace> {
    private BoardGroup boardGroup;
    private int column_row_number;
    private PathState target;

    public GameEnv2(){
        column_row_number = Constant.size;
        target = new PathState(Constant.size - 1,Constant.size-1);
        boardGroup = new BoardGroup(Constant.size,Constant.size,new PathState(0,0),target,new Random(1));
    }

    private DiscreteSpace actionSpace = new DiscreteSpace(8);
    private ObservationSpace<PathState> observationSpace = new ArrayObservationSpace(new int[]{1});
    @Override
    public ObservationSpace<PathState> getObservationSpace() {
        return observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return actionSpace;
    }

    @Override
    public PathState reset() {
        boardGroup.reset();
        return boardGroup.getPlayerPosition();
    }

    @Override
    public void close() {

    }

    @Override
    public StepReply<PathState> step(Integer i) {
        PathState playerPosition = boardGroup.getPlayerPosition();
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        if (i == 0){
            x = x + 1;
        }else if (i==1){
            y = y + 1;
        }else if (i==2){
            x = x - 1;
        }else if (i==3){
            y = y - 1;
        }else if (i==4){
            x = x + 1;
            y = y + 1;
        }else if (i==5){
            x = x - 1;
            y = y + 1;
        }else if (i==6){
            x = x - 1;
            y = y - 1;
        }else if (i==7){
            x = x + 1;
            y = y - 1;
        }
        PathState pathState = new PathState(x, y);
        boardGroup.changePlayerPosition(pathState);

        int current_reward = 3;
        if (target.getX() == x && target.getY() == y) {
            current_reward = 10;
        }else {
            int[] obstacleStates = boardGroup.getObstacleStates();
            for (int obstacleState : obstacleStates) {
                int tempX = obstacleState%column_row_number;
                int tempY = obstacleState/column_row_number;
                if (tempX == x && tempY == y){
                    current_reward = -5;
                }
            }
        }
        if (pathState.getX()<0||pathState.getX()>=Constant.size){
            current_reward = -20;
        }
        if (pathState.getY()<0||pathState.getY()>=Constant.size){
            current_reward = -20;
        }
        return new StepReply<>(pathState, current_reward, isDone(), null);
    }

    @Override
    public boolean isDone() {
        return boardGroup.isFinish();
    }

    @Override
    public MDP<PathState, Integer, DiscreteSpace> newInstance() {
        return new GameEnv2();
    }
}
