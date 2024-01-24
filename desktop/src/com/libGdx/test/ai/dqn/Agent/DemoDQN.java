package com.libGdx.test.ai.dqn.Agent;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.libGdx.test.ai.path.BoardGroup;
import com.libGdx.test.ai.path.Constant;
import com.libGdx.test.ai.path.PathState;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class DemoDQN extends DQN{
    private PathState start;
    private PathState target;
    BoardGroup boardGroup;
    private Random randomGenerator;
    final static int[] topology = {1, 70, 60, 4};
    public DemoDQN(double learningRate, double discountFactor) {
        super(topology, learningRate, discountFactor);
        start = new PathState(0,0);
        this.randomGenerator = new Random();
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
            return;
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
        getBoardGroup().getLegalAction();
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
        if (equal){
            Constant.succesTimes++;
        }
        return equal;
    }

    @Override
    protected double executeActionAndGetReward(int actionIndex) {
        PathState playerPosition = getBoardGroup().getPlayerPosition();
        PathState newPathState;
        if (actionIndex==0){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY());
        }else if (actionIndex == 1){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()+1);
        }else if (actionIndex == 2){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY());
        }else if(actionIndex == 3){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()-1);
        }else {
            newPathState = new PathState(playerPosition.getX(),playerPosition.getY());
            System.out.println("error ------------");
        }
        getBoardGroup().changePlayerPosition(newPathState);

        int[] obstacleStates = getBoardGroup().getObstacleStates();
        int state = playerPosition.getState();
        for (int obstacleState : obstacleStates) {
            if (state == obstacleState) {
                return -15;
            }
        }

        if (playerPosition.isEqual(target)) {
            return 10;
        }
        return 1;
    }

    public void stepBest(){
        PathState playerPosition = getBoardGroup().getPlayerPosition();
        int actionIndex = eval_step();
        PathState newPathState;
        if (actionIndex==0){
            newPathState = new PathState(playerPosition.getX()+1, playerPosition.getY());
        }else if (actionIndex == 1){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()+1);
        }else if (actionIndex == 2){
            newPathState = new PathState(playerPosition.getX()-1, playerPosition.getY());
        }else if(actionIndex == 3){
            newPathState = new PathState(playerPosition.getX(), playerPosition.getY()-1);
        }else {
            newPathState = new PathState(playerPosition.getX(),playerPosition.getY());
            System.out.println("error ------------");
        }
        getBoardGroup().showPath(newPathState);
    }


    public static void saveSnakeDQN(DemoDQN demoDQN,String filePath) {
        try(FileOutputStream fout = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);) {
            oos.writeObject(demoDQN);
        }
        catch(Exception e) {
            System.err.println("ERROR: Failure in saving network to " + filePath + ". Reason is " + e.getMessage());
        }
    }

    public static DemoDQN loadSnakeDQN(String filePath) {
        try(FileInputStream fin = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fin);) {
            return (DemoDQN) ois.readObject();
        }
        catch(Exception e) {
            System.err.println("ERROR: Failure in loading network from " + filePath + ". Reason is " + e.getMessage());
        }
        return null;
    }


    public BoardGroup getBoardGroup() {
        return boardGroup;
    }
}
