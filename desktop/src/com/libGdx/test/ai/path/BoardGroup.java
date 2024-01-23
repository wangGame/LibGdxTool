package com.libGdx.test.ai.path;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardGroup extends Group {
    private int column_row_number;
    private float cornerLengthY;
    private float cornerLengthX;

    private int state_number;
    private int obstacle_rate;
    private int[] obstacleStates;

    private int[][] R_matrisi;
    private double[][] Q_matrisi;
    private double y;
    private int normal_reward;
    private int hole_reward;
    private int target_reward;

    private PathState start;
    private PathState target;
    private PathState playerPosition;

    private Color startColor;
    private Color targetColor;
    private Color playerColor;
    private Color obstacleColor;
    private boolean leaveTrace;
    private FileWriter filewriter;
    private Random randomGenerator;
    private ArrayList<Integer> steps_per_episode;
    private ArrayList<Integer> costs_per_episode;
    private Image playerImg;
    public BoardGroup (int column_row_number, int obstacle_rate,PathState start,PathState target) {
        this.normal_reward = 3;
        this.hole_reward = -5;
        this.target_reward = 100;
        this.y =  0.9;
        this.startColor = new Color(0,0,255,50);
        this.targetColor = new Color(0,255,0,50);
        this.obstacleColor = new Color(240,14,14,50);
        this.playerColor = Color.BLUE;
        this.column_row_number = column_row_number;
        this.obstacle_rate = obstacle_rate;
        this.state_number = column_row_number * column_row_number;
        this.start = start;
        this.target = target;
        this.playerPosition = start;
        this.randomGenerator = new Random();
        try {
            filewriter = new FileWriter(new File("engel.txt"),false);
        } catch (IOException ex) {
            Logger.getLogger(BoardGroup.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * 玩家图片
         */
        playerImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        playerImg.setColor(Color.GREEN);
        addActor(playerImg);
//        初始化
        createBoard();
        createR();
        createObstacles();
        startQLearning(500);
    }

    private void createBoard(){
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
        this.cornerLengthX = cornerLength;
        this.cornerLengthY = cornerLength;
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
            tmpy= tmpy+cornerLengthY;
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
            tmpx= tmpx+cornerLengthX;
        }

        playerImg.setPosition((start.getX()*cornerLengthX),(start.getY()*cornerLengthY));
        Image endImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        endImg.setColor(targetColor);
        endImg.setPosition((target.getX()*cornerLengthX)+1,(target.getY()*cornerLengthY)+1);
        addActor(endImg);
    }


    private void createR(){
        //初始化每一个各自的价值 3
        R_matrisi = new int[state_number][state_number];
        for(int i = 0; i<state_number; i++){
            for(int j = 0; j<state_number; j++){
                R_matrisi[i][j] = normal_reward;
            }
        }
        for(int i = 0; i<state_number; i++){
            for(int j = 0; j<state_number; j++){
                //行为0
                if(i%column_row_number == 0){
                    if((i+1 != j)
                            &&(i+column_row_number != j)
                            &&(i+(column_row_number+1) != j)
                            &&(i-(column_row_number-1) != j)
                            &&(i-column_row_number != j))
                        R_matrisi[i][j] = -1;
                }
                else if((i+1)%column_row_number == 0){
                    //sağkenardaki blokların komşuları(sağ,sol,yukarı,aşağı,çaprazlar) haricindeki bloklar -1
                    if((i-1 != j)&&(i+column_row_number != j)&& (i+(column_row_number-1) != j)&&(i-(column_row_number+1) != j)&& (i-column_row_number != j))
                        R_matrisi[i][j] = -1;
                }//orta
                else{
                    //ortadaki blokların komşuları(sağ,sol,yukarı,aşağı,çaprazlar) haricindeki bloklar -1
                    if((i-1 != j)
                            &&(i+1 != j)
                            &&(i+column_row_number != j)
                            && (i+column_row_number-1 != j)
                            &&(i+column_row_number+1 != j)&&(i-column_row_number+1 != j)&& (i-column_row_number != j)&&(i-column_row_number-1 != j))
                        R_matrisi[i][j] = -1;
                }

            }
        }
        //targeta 20 değeri verilir.
        for(int i = 0; i<state_number; i++){
            for(int j = 0; j<state_number; j++){
                if((R_matrisi[i][j] != -1)&&(j == target.getState())){
                    R_matrisi[i][j] = target_reward;
                }
            }
        }
    }


    private void createObstacles(){
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

            //kazanç matrisinde eğer j state'inden gidilebilecek bir state ise değeri hole_reward yapılır.
            for(int j = 0;j<state_number;j++){
                if(R_matrisi[j][random] == normal_reward){
                    R_matrisi[j][random]= hole_reward;
                }
            }
            obstacleStates[i]=random;
            //x = column , y = row;
            drawObstacle(new PathState(random%column_row_number,random/column_row_number));
            i++;
        }
        try {
            filewriter.close();
        } catch (IOException ex) {
            Logger.getLogger(BoardGroup.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private void drawObstacle(PathState koordinat){
        Image obstacleImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        obstacleImg.setColor(obstacleColor);
        obstacleImg.setPosition((koordinat.getX()*cornerLengthX),(koordinat.getY()*cornerLengthY));
        obstacleImg.setSize(cornerLengthX,cornerLengthX);
        addActor(obstacleImg);


        try {
            filewriter.write(koordinat.toString());

        } catch (IOException ex) {
            Logger.getLogger(BoardGroup.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private int episode = 0;

    private void startQLearning(int episode_length){
        this.Q_matrisi = new double[state_number][state_number];
        for(int i = 0; i<state_number;i++){
            for(int j = 0; j<state_number; j++){
                Q_matrisi[i][j] = 0;
            }
        }
        this.steps_per_episode = new ArrayList<>();
        this.costs_per_episode = new ArrayList<>();

        xxx();


        System.out.println("iterasyon bitti.");
        /*
        System.out.println("step array size:"+steps_per_episode.size());
        for(int i = 0;i<costs_per_episode.size();i++){
            System.out.print(costs_per_episode.get(i)+" ");
        }
        System.out.println("cost array size:"+costs_per_episode.size());
        */
        //Daha sonra başlangıç noktasından başlanarak. maks maliyetli yol çizdirilir.

        System.out.println("yol çizdiriliyor...");
//        drawPath();
        System.out.println("yol çizdirilmesi tamamlandı.");

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

    public void xxx(){
        step = 0;
        if (episode>500){
            leaveTrace = true;
            drawPath();

            return;
        }
        leaveTrace = false;

        //her episode'da rastgele bir state üzerinden başlanır.
        //set_inital_state fonksiyonu playerPosition koordinatını rastgele bir state üzerine getirir.
        //为true说明结束了，那么就进行下一局
        if(set_initial_state()){
            addAction(Actions.delay(0.05f,Actions.run(()->{
                xxx();
            })));
            return;
        }
        initialize_episode();
    }


    private void changePlayerPosition(PathState targetPosition){
        /*
        if(playerPosition.isEqual(targetPosition)){
           return;
        }
        */
        if(!leaveTrace){


            playerImg.setPosition((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3);

//            graphics.clearRect((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,
//            (playerPosition.getY()*cornerLengthY)+cornerLengthY/3,cornerLengthX/3,cornerLengthY/2);
            boolean isFilled = false;
            for(int obstacle: obstacleStates){
                if(playerPosition.getState() == obstacle){
//                    graphics.setColor(obstacleColor);
//                    graphics.fillRect((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3,cornerLengthX/3,cornerLengthY/2);



                    playerImg.setPosition((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3);

                    isFilled = true;
                    break;

                }
            }
            if(isFilled == false){
                if(playerPosition.getState() == start.getState()){

                    playerImg.setPosition((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3);

//                    graphics.setColor(startColor);
//                    graphics.fillRect((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3,cornerLengthX/3,cornerLengthY/2);
                }
                else if(playerPosition.getState() == target.getState()){

                    playerImg.setPosition((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3);
//                    graphics.setColor(targetColor);
//                    graphics.fillRect((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3,cornerLengthX/3,cornerLengthY/2);
                }
            }
        }
//        graphics.setColor(playerColor);
//        graphics.fillRect((targetPosition.getX()*cornerLengthX)+cornerLengthX/3,(targetPosition.getY()*cornerLengthY)+cornerLengthY/3,cornerLengthX/3,cornerLengthY/2);

        playerImg.setPosition((playerPosition.getX()*cornerLengthX)+cornerLengthX/3,(playerPosition.getY()*cornerLengthY)+cornerLengthY/3);
        playerPosition = targetPosition;
    }


    private void changePlayerPosition1(PathState targetPosition){
        System.out.println("position :"+ targetPosition.getX() +"  "+targetPosition.getY());
        Image playerImg = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
        playerImg.setColor(Color.RED);
        addActor(playerImg);
        playerImg.setSize(100,100);
        playerImg.setPosition((targetPosition.getX()*cornerLengthX)+cornerLengthX/3,(targetPosition.getY()*cornerLengthY)+cornerLengthY/3);

        pathState = targetPosition;
    }

    private double maksQ(int state){
        //state üzerinden geçilebilecek state'ler bulunur.
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

    private boolean set_initial_state(){
        int randomPosition = randomGenerator.nextInt(state_number);
        boolean isObstacle0 = false;
        for(int obstacle: obstacleStates){
            if(obstacle == randomPosition){
                isObstacle0 = true;
            }
        }
        if(isObstacle0 ||(randomPosition == target.getState())){
            return true;
        }
        changePlayerPosition(new PathState(randomPosition%column_row_number,randomPosition/column_row_number));
        return false;
    }
    int cost = 0;
    int step = 0;
    private void initialize_episode(){
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
        double q_value = current_reward + (y * next_reward);
        Q_matrisi[playerPosition.getState()][nextState] = q_value;
        changePlayerPosition(new PathState(nextState%column_row_number,nextState/column_row_number));
        step++;
        //eğer target'a vardıysak
        if(playerPosition.isEqual(target)){
            cost += target_reward;
            costs_per_episode.add(cost);
            steps_per_episode.add(step);


            System.out.println(episode+"--------------------------"+step);
            episode++;

            addAction(Actions.delay(0.05f,Actions.run(()->{
                xxx();
            })));
            return;
        }

        //eğer bir obstacle'a çarparsa başa döner gidilen stateler listesi temizlenir.
        boolean isObstacle = false;
        for(int obstacle:obstacleStates){
            if(obstacle == nextState){
                isObstacle = true;
            }
        }

        if(isObstacle){
            cost += hole_reward;
            costs_per_episode.add(cost);
            steps_per_episode.add(step);
            addAction(Actions.delay(0.05f,Actions.run(()->{
                xxx();
            })));
            return;
        }
        cost += normal_reward;
        addAction(Actions.delay(0.05f,Actions.run(()->{
            initialize_episode();
        })));


    }

    boolean isTarget = false;
    private PathState pathState;
    /**
     * 从状态中获取到最大的Q
     */
    private void drawPath(){
        //oyunca başa alınır.
        pathState = start;
//        while(!isTarget){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(BoardGroup.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        c();
    }

    public void c(){
        if (!isTarget){
            addAction(Actions.delay(2,Actions.run(()->{
                c();
            })));
        }
        //oyuncunun gidebileceği stateler bulunur.
        //当前状态可以走的路
        int[] playerR = R_matrisi[pathState.getState()];
        ArrayList<Integer> statesFromPlayer = new ArrayList<>();
        for(int i = 0; i<playerR.length;i++){
            if((playerR[i] != -1)&&(playerR[i] != hole_reward)){
                statesFromPlayer.add(i);
            }
        }
        //二维数组
        double[] playerQ = Q_matrisi[pathState.getState()];
        double maks_q = playerQ[statesFromPlayer.get(0)];
        int nextState = statesFromPlayer.get(0);
        //选择Q值大的状态
        for(Integer state : statesFromPlayer ){
            if( playerQ[state] > maks_q){
                maks_q = playerQ[state];
                nextState = state;
            }
        }
        //改变位置
        changePlayerPosition1(new PathState(nextState%column_row_number,nextState/column_row_number));
        //success
        if(pathState.getState() == target.getState()){
            isTarget = true;
            System.out.println("hedefe varıldı.");
        }
    }
}
