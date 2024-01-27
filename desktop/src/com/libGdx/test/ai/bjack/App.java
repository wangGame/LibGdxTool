package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.ai.labyrinth.Constant;
import com.libGdx.test.base.LibGdxTestMain;

import org.junit.Test;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class App extends LibGdxTestMain {
    private HashMap<String,Integer> hashMap;
    public void setCardValue(){
        hashMap = new HashMap();
        hashMap.put("A",11);
        hashMap.put("2",2);
        hashMap.put("3",3);
        hashMap.put("4",4);
        hashMap.put("5",5);
        hashMap.put("6",6);
        hashMap.put("7",7);
        hashMap.put("8",8);
        hashMap.put("9",9);
        hashMap.put("10",10);
        hashMap.put("J",10);
        hashMap.put("Q",10);
        hashMap.put("K",10);
    }

    public int getScore(Array<String> array){
        int score = 0;
        int handA = 0;
        for (String s : array) {
            if (s.equals("A")){
                handA += 1;
            }
            score += hashMap.get(s);
        }
        while (score>21 && handA>0) {
            score -= 10;
            handA --;
        }
        return score;
    }

    public static void main(String[] args) {
//        App app = new App();
//        app.setCardValue();
//        Array<String> handCard = new Array<>();
//        handCard.add("6");
//        handCard.add("7");
//        handCard.add("A");
//        System.out.println(app.getScore(handCard));
//        app.start();
        Random random = new Random(1);
        BlackJackGame game = new BlackJackGame(random);
        game.initGame();
        Scanner scanner = new Scanner(System.in);
        while (true){
            int userScore = game.userScore();
            int dealScore = game.getJudger().judge_score(game.getDealer().getHand());
            if (Constant.isBug) {
                System.out.println("state: player " + userScore + "  dealer :" + dealScore);
                System.out.println("current player " + game.getGame_pointer() + " :");
            }
            String next = scanner.next();
            game.step(next);
            if (game.isOver()) {
                HashMap<String, Integer> winner = game.getWinner();
                for (String s : winner.keySet()) {
                    Integer integer = winner.get(s);
                    System.out.println(s+"===>"+integer);
                }
                break;
            }
        }
    }


    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

    }
}
