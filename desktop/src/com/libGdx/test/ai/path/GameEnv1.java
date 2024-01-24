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

public class GameEnv1 extends Group {
    private int state_number;
    private BoardGroup group;
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
    public GameEnv1 (int column_row_number, int obstacle_rate,PathState start,PathState target) {
        this.target = target;
        this.factor = 0.9f;
        this.column_row_number = column_row_number;
        //状态个数 [对特定棋盘的一个操作]
        this.state_number = column_row_number * column_row_number;
        this.randomGenerator = new Random(1);
        this.normal_reward = 3; //普通奖励
        this.hole_reward = -5; // 障碍扣分
        this.target_reward = 100; //目标奖励
        group = new BoardGroup(column_row_number,obstacle_rate,start,target,randomGenerator);
        group.createBoard();

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
        int[] legalAction = group.getLegalAction();
        ArrayList<Integer> statesFromPlayer = new ArrayList<>();

        for (int i : legalAction) {
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
            statesFromPlayer.add(PathState.calState(x,y));
        }

        //随机取一个状态
        int nextState = statesFromPlayer.get(randomGenerator.nextInt(statesFromPlayer.size()));
        //当前状态到下一个状态的奖励

        int x = nextState%column_row_number;
        int y = nextState/column_row_number;

        int current_reward = 3;
        if (target.getX() == x && target.getY() == y) {
            current_reward = 10;
        }else {
            int[] obstacleStates = group.getObstacleStates();
            for (int obstacleState : obstacleStates) {
                int tempX = obstacleState%column_row_number;
                int tempY = obstacleState/column_row_number;
                if (tempX == x && tempY == y){
                    current_reward = -5;
                }
            }
        }
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
            System.out.println(episode+"========episode times");
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
//        int[] playerR = R_matrisi[state];
//        ArrayList<Integer> statesFromState = new ArrayList<>();
//        for(int i = 0; i<playerR.length;i++){
//            if(playerR[i] != -1){
//                statesFromState.add(i);
//            }
//        }
        int x1 = state%column_row_number;
        int y1 = state/column_row_number;
        int[] legalAction = group.getLegalAction(new PathState(x1,y1));
        ArrayList<Integer> statesFromState = new ArrayList<>();


        for (int i : legalAction) {
            int x = state%column_row_number;
            int y = state/column_row_number;
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
            statesFromState.add(PathState.calState(x,y));
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
                save("migong.skl");
            } catch (Exception e) {
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

    }

    private void createR() {
        //初始化每一个各自的价值 3

    }

    public void save(String fileName){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this.Q_matrisi); // FIXME: TreeMaps cannot be serialized.
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadModel (String fileName){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            this.Q_matrisi = (double[][]) ois.readObject(); // FIXME: TreeMaps cannot be serialized.
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void drawPath() {
        if (getGroup().isFinish()) {
            System.out.println("success~");
        }else {
            evalStep();
            addAction(Actions.delay(2,Actions.run(()->{
                drawPath();
            })));
        }
    }

    private void evalStep(){
        PathState playerPosition = group.getPlayerPosition();
        int[] legalAction = group.getLegalAction();
        ArrayList<Integer> statesFromPlayer = new ArrayList<>();
        for (int i : legalAction) {
            int x = playerPosition.getX();
            int y = playerPosition.getY();
            if (i == 0) {
                x = x + 1;
            } else if (i == 1) {
                y = y + 1;
            } else if (i == 2) {
                x = x - 1;
            } else if (i == 3) {
                y = y - 1;
            } else if (i == 4) {
                x = x + 1;
                y = y + 1;
            } else if (i == 5) {
                x = x - 1;
                y = y + 1;
            } else if (i == 6) {
                x = x - 1;
                y = y - 1;
            } else if (i == 7) {
                x = x + 1;
                y = y - 1;
            }
            int i1 = PathState.calState(x, y);

            statesFromPlayer.add(i1);
        }
//        int[] playerR = R_matrisi[playerPosition.getState()];
//        for(int i = 0; i<playerR.length;i++){
//            if((playerR[i] != -1)&&(playerR[i] != hole_reward)){
//                statesFromPlayer.add(i);
//            }
//        }

        double[] playerQ = Q_matrisi[playerPosition.getState()];
        double maks_q = playerQ[statesFromPlayer.get(0)];
        int nextState = statesFromPlayer.get(0);
        //随机取一个状态
        for(Integer state : statesFromPlayer ){
            if( playerQ[state] > maks_q){
                maks_q = playerQ[state];
                nextState = state;
            }
        }
        group.changePlayerPosition(new PathState(nextState%column_row_number,nextState/column_row_number));
    }
}