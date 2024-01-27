package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.ai.labyrinth.Constant;
import com.libGdx.test.chat.ChatData;
import com.libGdx.test.chat.ChatGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AppQLearning extends Group {
    private HashMap<String, ArrayList<Double>> Q_matrisi; //0 hit 1 stand
    private Random randomGenerator;
    private int episode = 0;
    private float factor;
    private BlackJackGame game;

    public AppQLearning() {
//        this.factor = 0.928f;
        this.factor = 0.9f;
        this.randomGenerator = new Random();
        game = new BlackJackGame(randomGenerator);
        try {
            writer = new FileWriter(new File("successP.csv"));
            builder.setLength(0);
            builder.append("id,");
            builder.append("value");
            builder.append("\r\n");
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startQLearning() {
        this.Q_matrisi = new HashMap<>();
        //初始化
        for (int i = 0; i <= 31; i++) {
            for (int i1 = 0; i1 <= 31; i1++) {
                ArrayList<Double> array = new ArrayList<>();
                for (int i2 = 0; i2 < 2; i2++) {
                    array.add(0.0);
                }
                Q_matrisi.put(i + "" + i1, array);
            }
        }
        gameRound();
    }

    private void set_initial_state() {
        game.initGame();
        game.showCard();
    }

    private int bb = 0;
    private void initialize_episode() {
        BlackStatus state = game.getState(game.getGame_pointer());
        int[] legalAction = state.getLegalAction();

        int bestAction;
        int v = (int) (randomGenerator.nextInt(legalAction.length) * (1.0f / (episode + 1)));
//        if (Math.random() * episode/com.libGdx.test.ai.labyrinth.Constant.learnTimes<0.5) {
//            bestAction = legalAction[randomGenerator.nextInt(legalAction.length)];
//
//        }else {
//            bestAction = best(state);
//        }
        bestAction = (int) ((best(state) + v)%legalAction.length);
        game.step(bestAction);
        HashMap<String, Integer> winner = game.getWinner();
        int current_reward = winner.get("player" + game.getGame_pointer());
        if (Constant.isBug) {
            System.out.println(current_reward);
            game.showAction(bestAction);
            game.showCard();
        }
        BlackStatus newStatus = game.getState(game.getGame_pointer());
        double next_reward = maksQ(newStatus.getHandCard());

        ArrayList<Double> doubles = Q_matrisi.get(newStatus.getHandCard());
        Double aDouble = doubles.get(bestAction);
        double q_value = aDouble + 0.0018* (current_reward+( factor * next_reward)-aDouble);
//        double q_value = current_reward+factor * next_reward;

        doubles.set(bestAction, q_value);
        if (bestAction==1){
            bb++;
        }
        //eğer target'a vardıysak
        if (game.isOver()) {
            game.winnerInfo();
            episode++;
            addAction(Actions.delay(com.libGdx.test.ai.labyrinth.Constant.baseSpeed, Actions.run(() -> {
                gameRound();

            })));
            return;
        }
        addAction(Actions.delay(com.libGdx.test.ai.labyrinth.Constant.baseSpeed, Actions.run(() -> {
            initialize_episode();

        })));
    }

    private double maksQ(String state) {
        ArrayList<Double> doubles = Q_matrisi.get(state);
        Double q_maks = doubles.get(0);
        for (Double aDouble1 : doubles) {
            if (q_maks > aDouble1) {
                q_maks = aDouble1;
            }
        }
        return q_maks;
    }

    private int  allTimes = 0;

    private FileWriter writer;

    public void gameRound() {
        allTimes++;

        if (episode > com.libGdx.test.ai.labyrinth.Constant.learnTimes) {
            System.out.println("fijish~"+episode*1.0f/allTimes);
            try {
                save("blackj.skl");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(bb+"-----------------------------------");
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadModel("../blackj.skl",getStage());
            return;
        }
        //为true说明结束了，那么就进行下一局
        //开始之前，随便来一个位置进行训练
        set_initial_state();
        initialize_episode();

        playSuccessTimes = 0;
//        for (int i = 0; i < 500; i++) {
//            BlackJackGame game = new BlackJackGame(new Random());
//            game.initGame();
//            while (!game.isOver()){
//                userStep(game);
//            }
//            HashMap<String, Integer> winner = game.getWinner();
//            Integer integer = winner.get("player"+0);
//            if (integer>0) {
//                playSuccessTimes++;
//            }
//        }
//        float xx = playSuccessTimes*1.f/ 500;
////        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
////        System.out.println(xx);
////        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//        try {
//            builder.setLength(0);
//            builder.append(allTimes);
//            builder.append(","+xx);
//            builder.append("\r\n");
//            writer.write(builder.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private StringBuilder builder = new StringBuilder();
    private int playSuccessTimes;

    public void save(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this.Q_matrisi); // FIXME: TreeMaps cannot be serialized.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModel(String fileName, Stage stage) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            this.Q_matrisi = (HashMap<String, ArrayList<Double>>) ois.readObject(); // FIXME: TreeMaps cannot be serialized.
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (String s : Q_matrisi.keySet()) {
//            ArrayList<Double> doubles = Q_matrisi.get(s);
//            builder.setLength(0);
//            for (Double aDouble : doubles) {
//                builder.append(s+"==="+aDouble);
//            }
//            System.out.println(builder.toString());
//        }
        System.out.println("-----------------------");


        playTIme();


        ChatGroup group = new ChatGroup(cc);
        group.showView();
        stage.addActor(group);

    }

    Array<ChatData> cc = new Array<>();

    public void playTIme(){
        int xxX = 0;

        double ddd = 0;
        for (int i1 = 0; i1 < 100; i1++) {
            xxX ++;  playSuccessTimes = 0;
            for (int i = 0; i < 10000; i++) {
                BlackJackGame game = new BlackJackGame(new Random());
                game.initGame();
                while (!game.isOver()){
                    userStep(game);
                }
                HashMap<String, Integer> winner = game.getWinner();
                Integer integer = winner.get("player"+0);
                if (integer>0) {
                    playSuccessTimes++;
                }
            }
            float xx = playSuccessTimes*1.f/ 10000;
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//        System.out.println(xx);
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            try {
                ddd += xx;
                builder.setLength(0);
                builder.append(xxX);
                builder.append(","+xx);
                builder.append("\r\n");
                writer.write(builder.toString());
                ChatData chatData = new ChatData();
                chatData.setId(xxX);
                chatData.setValue(xx);
                cc.add(chatData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        double oo = ddd / 100.0;
        try {
            builder.setLength(0);
            builder.append("alll");
            builder.append(","+oo);
            System.out.println("all "+oo);
            builder.append("\r\n");
            writer.write(builder.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finish");
    }


    public void userStep(BlackJackGame game){
        BlackStatus state = game.getState(game.getGame_pointer());
        int bestAction = best(state);
        game.step(bestAction);
    }

    private int best(BlackStatus state) {
        ArrayList<Double> doubles = Q_matrisi.get(state.getHandCard());
        Double aDouble = doubles.get(0);
        int bestAction = 0;
        for (int i = 0; i < doubles.size(); i++) {
            Double aDouble1 = doubles.get(i);
            if (aDouble<aDouble1){
                aDouble = aDouble1;
                bestAction = i;
            }
        }
        return bestAction;


//        int i = randomGenerator.nextInt(2);
//        return i;
    }
}