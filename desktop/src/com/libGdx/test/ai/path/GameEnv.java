package com.libGdx.test.ai.path;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class GameEnv extends Group {
    private int state_number;
    private BoardGroup group;
    private int[][]R_matrisi;
    private int column_row_number;
    private int normal_reward;
    private int hole_reward;
    private int target_reward;
    private double[][] Q_matrisi;
    private PathState target;
    private ArrayList<Integer> steps_per_episode;
    private ArrayList<Integer> costs_per_episode;
    private Random randomGenerator;
    private int episode = 0;
    private float factor;
    private int cost = 0;
    private int step = 0;
    /**
     * @param column_row_number 棋盘大小
     * @param obstacle_rate 障碍率
     * @param start 开始位置
     * @param target 结束位置
     */
    public GameEnv (int column_row_number, int obstacle_rate,PathState start,PathState target) {
        this.target = target;
        this.factor = 0.9f;
        this.column_row_number = column_row_number;
        //状态个数 [对特定棋盘的一个操作]
        this.state_number = column_row_number * column_row_number;
        this.randomGenerator = new Random();
        this.normal_reward = 3; //普通奖励
        this.hole_reward = -5; // 障碍扣分
        this.target_reward = 100; //目标奖励
        group = new BoardGroup(column_row_number,obstacle_rate,start,target,randomGenerator);
        group.createBoard();
        createR();
        group.createObstacles();
        initObstacle();
        group.initStart();
        group.initPlayStatus();
    }

    public void startQLearning(){
        this.Q_matrisi = new double[state_number][state_number];
        for(int i = 0; i<state_number;i++){
            for(int j = 0; j<state_number; j++){
                Q_matrisi[i][j] = 0;
            }
        }
        this.steps_per_episode = new ArrayList<>();
        this.costs_per_episode = new ArrayList<>();
        gameRound();
    }

    public BoardGroup getGroup() {
        return group;
    }

    private boolean set_initial_state(){
        int randomPosition = randomGenerator.nextInt(state_number);
        boolean isObstacle0 = false;
        for(int obstacle: group.getObstacleStates()){
            if(obstacle == randomPosition){
                isObstacle0 = true;
            }
        }
        if(isObstacle0 ||(randomPosition == target.getState())){
            return true;
        }
        group.changePlayerPosition(new PathState(randomPosition%column_row_number,randomPosition/column_row_number));
        return false;
    }

    private void initialize_episode(){
        PathState playerPosition = group.getPlayerPosition();
        int[] playerR = R_matrisi[playerPosition.getState()];
        ArrayList<Integer> statesFromPlayer = new ArrayList<>();
        for(int i = 0; i<playerR.length;i++){
            if(playerR[i] != -1){
                statesFromPlayer.add(i);
            }
        }
        //随机取一个状态
        int nextState = statesFromPlayer.get(randomGenerator.nextInt(statesFromPlayer.size()));
        //当前状态到下一个状态的奖励
        int current_reward = R_matrisi[playerPosition.getState()][nextState];
        double next_reward = maksQ(nextState);
        double q_value = current_reward + (factor * next_reward);
        Q_matrisi[playerPosition.getState()][nextState] = q_value;
        group.changePlayerPosition(new PathState(nextState%column_row_number,nextState/column_row_number));
        step++;
        //eğer target'a vardıysak
        if(playerPosition.isEqual(target)){
            cost += target_reward;
            costs_per_episode.add(cost);
            steps_per_episode.add(step);
            episode++;
            addAction(Actions.delay(com.libGdx.test.ai.path.Constant.baseSpeed,Actions.run(()->{
                gameRound();
            })));
            return;
        }
        boolean isObstacle = false;
        for(int obstacle:group.getObstacleStates()){
            if(obstacle == nextState){
                isObstacle = true;
            }
        }
        if(isObstacle){
            cost += hole_reward;
            costs_per_episode.add(cost);
            steps_per_episode.add(step);
            addAction(Actions.delay(com.libGdx.test.ai.path.Constant.baseSpeed,Actions.run(()->{
                gameRound();
            })));
            return;
        }
        cost += normal_reward;
        addAction(Actions.delay(com.libGdx.test.ai.path.Constant.baseSpeed,Actions.run(()->{
            initialize_episode();
        })));
    }

    private double maksQ(int state){
        int[] playerR = R_matrisi[state];
        ArrayList<Integer> statesFromState = new ArrayList<>();
        for(int i = 0; i<playerR.length;i++){
            if(playerR[i] != -1){
                statesFromState.add(i);
            }
        }

        double q_maks = Q_matrisi[state][statesFromState.get(0)];
        for(int i = 0;i<statesFromState.size();i++){
            if(q_maks < Q_matrisi[state][statesFromState.get(i)]){
                q_maks = Q_matrisi[state][statesFromState.get(i)];
            }
        }
        return q_maks;
    }

    public void gameRound(){
        step = 0;
        if (episode> com.libGdx.test.ai.path.Constant.learnTimes){
            System.out.println("success~");
            try {
                save(new URL("migong.skl"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return;
        }
        //为true说明结束了，那么就进行下一局
        //开始之前，随便来一个位置进行训练
        if(set_initial_state()){
            addAction(Actions.delay(0.05f,Actions.run(()->{
                gameRound();
            })));
            return;
        }
        initialize_episode();
    }

    private void initObstacle() {
        int[] obstacleStates = group.getObstacleStates();
        for(int j = 0;j<state_number;j++){
            for (int random : obstacleStates) {
                if(R_matrisi[j][random] == normal_reward){
                    R_matrisi[j][random]= hole_reward;
                }
            }
        }
    }

    private void createR() {
        //初始化每一个各自的价值 3
        R_matrisi = new int[state_number][state_number];
        for(int i = 0; i<state_number; i++){
            for(int j = 0; j<state_number; j++){
                R_matrisi[i][j] = normal_reward;
            }
        }
        for(int i = 0; i<state_number; i++){
            for(int j = 0; j<state_number; j++){
                if(i%column_row_number == 0){
                    if((i+1 != j)
                            &&(i+column_row_number != j)
                            &&(i+(column_row_number+1) != j)
                            &&(i-(column_row_number-1) != j)
                            &&(i-column_row_number != j))
                        R_matrisi[i][j] = -1;
                }
                else if((i+1)%column_row_number == 0){
                    if((i-1 != j)&&(i+column_row_number != j)&& (i+(column_row_number-1) != j)&&(i-(column_row_number+1) != j)&& (i-column_row_number != j))
                        R_matrisi[i][j] = -1;
                }else{
                    if((i-1 != j)
                            &&(i+1 != j)
                            &&(i+column_row_number != j)
                            && (i+column_row_number-1 != j)
                            &&(i+column_row_number+1 != j)&&(i-column_row_number+1 != j)&& (i-column_row_number != j)&&(i-column_row_number-1 != j))
                        R_matrisi[i][j] = -1;
                }
            }
        }
        for(int i = 0; i<state_number; i++){
            for(int j = 0; j<state_number; j++){
                if((R_matrisi[i][j] != -1)&&(j == target.getState())){
                    R_matrisi[i][j] = target_reward;
                }
            }
        }
    }

    public void save(URL fileName){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName.getPath()))) {
            oos.writeObject(this.Q_matrisi); // FIXME: TreeMaps cannot be serialized.
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadModel (URL fileName){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName.getPath()))) {
            this.Q_matrisi = (double[][]) ois.readObject(); // FIXME: TreeMaps cannot be serialized.
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}