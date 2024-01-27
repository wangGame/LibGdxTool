package com.libGdx.test.ai.feii.dqn.Agent;

import com.libGdx.test.ai.labyrinth.BoardGroup;
import com.libGdx.test.ai.labyrinth.Constant;
import com.libGdx.test.ai.labyrinth.PathState;

import java.util.Random;

public class DemoDQN extends DQN{
    private PathState start;
    private PathState target;
    BoardGroup boardGroup;

    final static int[] topology = {1,70,50,20, 8};
    public DemoDQN(double learningRate, double discountFactor) {
        super(topology, learningRate, discountFactor);
        start = new PathState(0,0);
        this.randomGenerator = new Random(1);
        target = new PathState((int) (Constant.size * 0.5f), (int) (Constant.size*0.5f));
        boardGroup = new BoardGroup(Constant.size,Constant.size,start,target,randomGenerator);

    }

    public void reset(){
        //确定初始位置
        int randomPosition = randomGenerator.nextInt(Constant.size*Constant.size);
        boolean isObstacle0 = false;
        for(int obstacle: boardGroup.getObstacleStates()){
            if(obstacle == randomPosition){
                isObstacle0 = true;
            }
        }
        if(isObstacle0 ||(randomPosition == target.getState())){
            reset();
        }

        boardGroup.changePlayerPosition(new PathState(randomPosition%Constant.size,randomPosition/Constant.size));
    }

    @Override
    protected double[] getState() {
        PathState playerPosition = boardGroup.getPlayerPosition();
        int state = playerPosition.getState();
        return new double[]{state};
    }

    @Override
    protected GameState getGameState() {
        GameState gameState = new GameState();
        int state = getBoardGroup().getPlayerPosition().getState();
        double[] doubles = {state};
        gameState.setState(doubles);
        gameState.setLegalActions(getBoardGroup().getLegalAction());
        return gameState;
    }

    @Override
    protected boolean isDone() {
        PathState playerPosition = boardGroup.getPlayerPosition();
        int[] obstacleStates = getBoardGroup().getObstacleStates();
        int state = playerPosition.getState();
        for (int obstacleState : obstacleStates) {
            if (state == obstacleState) {
                return true;
            }
        }
        boolean equal = playerPosition.isEqual(target);
        return equal;
    }

    @Override
    protected double executeActionAndGetReward(int actionIndex) {
        PathState playerPosition = getBoardGroup().getPlayerPosition();
        PathState newPathState = null;
        if (actionIndex==0){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY());
        }else if (actionIndex == 1){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()+1);
        }else if (actionIndex == 2){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY());
        }else if(actionIndex == 3){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()-1);
        }else if (actionIndex == 4){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY()+1);
        }else if (actionIndex == 5){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY()+1);
        }else if (actionIndex == 6){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY()-1);
        }else if (actionIndex == 7){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY()-1);
        }
        getBoardGroup().changePlayerPosition(newPathState);
        int[] obstacleStates = getBoardGroup().getObstacleStates();
        int state = newPathState.getState();

        for (int obstacleState : obstacleStates) {
            if (state == obstacleState) {
                return -5;
            }
        }

        if (newPathState.isEqual(target)) {
            return 100;
        }
        return 3;
    }

    public void stepBest(){
        PathState playerPosition = getBoardGroup().getPlayerPosition();
        int actionIndex = eval_step();
        PathState newPathState = null;
        if (actionIndex==0){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY());
        }else if (actionIndex == 1){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()+1);
        }else if (actionIndex == 2){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY());
        }else if(actionIndex == 3){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()-1);
        }else if(actionIndex == 4){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY()+1);
        }else if(actionIndex == 5){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY()+1);
        }else if(actionIndex == 6){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY()-1);
        }else if(actionIndex == 7){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY()-1);
        }
        System.out.println(actionIndex);
        getBoardGroup().showPath(newPathState);
    }

    public BoardGroup getBoardGroup() {
        return boardGroup;
    }

    public boolean isSuccess(){
        PathState playerPosition = getBoardGroup().getPlayerPosition();
        if (playerPosition.isEqual(target)) {
            return true;
        }
        return false;
    }
}
