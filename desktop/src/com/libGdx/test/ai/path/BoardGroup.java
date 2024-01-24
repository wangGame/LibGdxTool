package com.libGdx.test.ai.path;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BoardGroup extends Group {
    private int column_row_number;
    private float cornerSize;
    private int obstacle_rate;
    private int[] obstacleStates;
    private int state_number;
    private PathState start;
    private PathState target;
    private PathState playerPosition;
    private Color startColor;
    private Color targetColor;
    private Color playerColor;
    private Color obstacleColor;
    private Random randomGenerator;
    private Image playerImg;

    /**
     * @param column_row_number 棋盘大小
     * @param obstacle_rate 障碍率
     * @param start 开始位置
     * @param target 结束位置
     */
    public BoardGroup (int column_row_number, int obstacle_rate,PathState start,PathState target,Random randomGenerator) {
        this.state_number = column_row_number * column_row_number;
        this.startColor = new Color(0,0,255,255);
        this.targetColor = new Color(0,255,0,255);
        this.obstacleColor = new Color(240,14,14,255);
        this.playerColor = Color.BLUE;
        this.column_row_number = column_row_number;
        this.obstacle_rate = obstacle_rate;
        this.start = start;
        this.target = target;
        this.playerPosition = start;
        this.randomGenerator = randomGenerator;
        init();
    }

    public void init(){
        createBoard();
        createObstacles();
        initStart();
        initPlayStatus();
    }

    public PathState getPlayerPosition() {
        return playerPosition;
    }

    public int[] getObstacleStates() {
        return obstacleStates;
    }

    public void initStart() {
        Image startFlag = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        startFlag.setColor(startColor);
        addActor(startFlag);
        startFlag.setSize(cornerSize,cornerSize);
    }

    public void initPlayStatus() {
        playerImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        playerImg.setColor(playerColor);
        addActor(playerImg);
        playerImg.setSize(cornerSize,cornerSize);
        playerImg.setPosition((start.getX()* cornerSize),(start.getY()*cornerSize));
    }

    public void createBoard(){
        float maxCornerLength1 = (Constant.GAMEWIDTH / column_row_number);
        float maxCornerLength2 = (Constant.GAMEHIGHT / column_row_number);
        float cornerLength=0;
        if(maxCornerLength1 < maxCornerLength2){
            cornerLength = maxCornerLength1;
        } else if(maxCornerLength2 < maxCornerLength1){
            cornerLength = maxCornerLength2;
        } else{
            cornerLength = maxCornerLength1;
        }
        //格子宽度
        this.cornerSize = cornerLength;
        //划线
        Color lineColor = Color.WHITE;
        float tmpx=0;
        float tmpy=0;
        for(int i = column_row_number;i>=0;i--){
            Image line = new Image(new NinePatch(
                    Asset.getAsset().getTexture("assets/white_cir.png"),2,2,2,2));
            line.setWidth(Constant.GAMEWIDTH);
            line.setHeight(9);
            line.setColor(lineColor);
            addActor(line);
            line.setPosition(tmpx,tmpy);
            tmpy= tmpy+cornerSize;
        }
        tmpx=0;
        tmpy=0;
        for(int i = column_row_number;i>=0;i--){
            Image line = new Image(new NinePatch(
                    Asset.getAsset().getTexture("assets/white_cir.png"),2,2,2,2));
            line.setHeight(Constant.GAMEWIDTH);
            line.setWidth(9);
            line.setColor(lineColor);
            addActor(line);
            line.setPosition(tmpx,tmpy);
            tmpx= tmpx+ cornerSize;
        }
        Image endImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        endImg.setColor(targetColor);
        endImg.setSize(cornerSize,cornerSize);
        endImg.setPosition((target.getX()* cornerSize),(target.getY()*cornerSize));
        addActor(endImg);
    }

    public void createObstacles(){
        int obstacle_number = state_number * obstacle_rate / 100;
        this.obstacleStates = new int[obstacle_number];
        System.out.println(obstacle_number);
        int i = 0;
        while(i<obstacle_number){
            int random = randomGenerator.nextInt(state_number);
            if((random == start.getState())||(random == target.getState())){
                continue;
            }
            boolean isObstacleExists = false;
            for(int obstacle:obstacleStates){
                if(obstacle == random){
                    isObstacleExists = true;
                    break;
                }
            }
            if(isObstacleExists){
                continue;
            }
            obstacleStates[i]=random;
            //x = column , y = row;
            drawObstacle(new PathState(random%column_row_number,random/column_row_number));
            i++;
        }
    }

    private void drawObstacle(PathState koordinat){
        Image obstacleImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        obstacleImg.setColor(obstacleColor);
        obstacleImg.setPosition((koordinat.getX()* cornerSize),(koordinat.getY()*cornerSize));
        obstacleImg.setSize(cornerSize, cornerSize);
        addActor(obstacleImg);
    }


    public void changePlayerPosition(PathState targetPosition){
        playerImg.addAction(Actions.moveTo(
                playerPosition.getX() * cornerSize,
                playerPosition.getY() * cornerSize, com.libGdx.test.ai.path.Constant.adj));
        playerPosition = targetPosition;
    }

    public void drawtargetPosition(PathState targetPosition){
        Image playerImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        playerImg.setColor(Color.RED);
        addActor(playerImg);
        playerImg.setSize(cornerSize,cornerSize);
        playerImg.setPosition((targetPosition.getX()* cornerSize),(targetPosition.getY()*cornerSize));
    }

    public boolean isFinish() {
        boolean isObstacle0 = false;
        for(int obstacle: getObstacleStates()){
            if(obstacle == playerPosition.getState()){
                isObstacle0 = true;
            }
        }
        if(isObstacle0 ||(playerPosition.getState() == target.getState())){
            return true;
        }
        return false;
    }

    public int[] getLegalAction(PathState playerPosition) {
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        Array<Integer> temp = new Array<>();
        if (x+1 >=0 && x+1<column_row_number){
            temp.add(0);
        }
        if (x-1 >=0 && x-1<column_row_number){
            temp.add(2);
        }
        if (y+1 >=0 && y+1<column_row_number){
            temp.add(1);
        }
        if (y-1 >=0 && y-1<column_row_number){
            temp.add(3);
        }
        int [] action = new int[temp.size];
        for (int i = 0; i < temp.size; i++) {
            action[i] = temp.get(i);
        }
        return action;
    }


    public boolean check(int v){
        if (v >=0 && v<column_row_number){
            return true;
        }else {
            return false;
        }
    }

    public int[] getLegalAction() {
        PathState playerPosition = getPlayerPosition();
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        Array<Integer> temp = new Array<>();
        //      5 1  4
        //       \|/
        //   2 -- m -- 0
        //       /|\
        //     6  3 7
        //0
        if (check(x+1)) {
           temp.add(0);
        }
        if (check(x-1)){
            temp.add(2);
        }
        if (check(y+1)){
            temp.add(1);
        }
        if (check(y-1)){
            temp.add(3);
        }
        if (check(y+1)&&check(x+1)){
            temp.add(4);
        }
        if (check(y+1)&&check(x-1)){
            temp.add(5);
        }
        if (check(y-1)&&check(x-1)){
            temp.add(6);
        }
        if (check(y-1)&&check(x+1)){
            temp.add(7);
        }
        int [] action = new int[temp.size];
        for (int i = 0; i < temp.size; i++) {
            action[i] = temp.get(i);
        }
        return action;
    }

    public void showPath(PathState target){
        Image playerImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        playerImg.setColor(Color.RED);
        addActor(playerImg);
        playerImg.setSize(cornerSize,cornerSize);
        playerImg.setPosition((target.getX()* cornerSize),(target.getY()*cornerSize));
        playerPosition = target;
    }
}
