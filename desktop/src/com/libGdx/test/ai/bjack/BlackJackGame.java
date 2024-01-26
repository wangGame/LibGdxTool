package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;

public class BlackJackGame {
    private boolean allowStepBack;
    private Random random;
    private int numPlayers ;
    private int numDecks;
    private int game_pointer;
    private Array history;
    private HashMap<String,Integer> winner;
    private Array<BlackjackPlayer> players;
    private BlackjackDealer dealer;
    private BlackjackJudger judger;

    public BlackJackGame(Random random){
        this.random = random;
        this.winner = new HashMap<>();
        this.numPlayers = 1;
    }

    public void initGame(){
        this.dealer = new BlackjackDealer(random,numDecks);
        this.players = new Array<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new BlackjackPlayer(i,random));
        }
        this.judger = new BlackjackJudger(random);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < numPlayers; j++) {
                dealer.dealCard(players.get(j).getHand());
            }
            dealer.dealCard(dealer.getHand());
        }

        for (int i = 0; i < numPlayers; i++) {
            players.get(i).setScore(
                    judger.judge_round1(players.get(i).getHand()));
            players.get(i).setStatus(
                    judger.judge_round2(players.get(i).getHand()));
        }

        judger.setScore(judger.judge_round1(dealer.getHand()));
        judger.setStatus(judger.judge_round2(dealer.getHand()));


        this.winner.put("dealer",0);
        for (int i = 0; i < numPlayers; i++) {
            winner.put("player" + i,0);
        }
        history = new Array();
        this.game_pointer = 0;
    }

    public int getGame_pointer() {
        return game_pointer;
    }

    public BlackjackJudger getJudger() {
        return judger;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int userScore(){
        return judger.judge_score(players.get(game_pointer).getHand());
    }

    public int getNumActions(){
        return 2;
    }

    public int getPlayerId(){
        return game_pointer;
    }

    public void step(int action){
        if(action == 1){
            step("hit");
        }else {
            step("stand");
        }
    }

    public void step(String action){
        if (!action.equals("stand")){
            BlackjackPlayer blackjackPlayer = players.get(game_pointer);
            Array<Card> hand = blackjackPlayer.getHand();
            dealer.dealCard(hand);
            int score = judger.judge_round1(hand);
            String status = judger.judge_round2(hand);
            blackjackPlayer.setStatus(status);
            blackjackPlayer.setScore(score);
            if (status.equals("bust")){
                if (game_pointer>=numPlayers-1){
                    while (judger.judge_score(dealer.getHand()) < 17){
                        dealer.dealCard(dealer.getHand());
                    }
                    int dealScore = judger.judge_round1(dealer.getHand());
                    String dealStatus = judger.judge_round2(dealer.getHand());
                    getDealer().setScore(dealScore);
                    getDealer().setStatus(dealStatus);
                    for (int i = 0; i < numPlayers; i++) {
                        judger.judgeGame(this,i);
                    }
                    game_pointer = 0;
                }else {
                    game_pointer += 1;
                }
            }
        }else {
            int score = judger.judge_round1(players.get(game_pointer).getHand());
            String status = judger.judge_round2(players.get(game_pointer).getHand());
            players.get(game_pointer).setScore(score);
            players.get(game_pointer).setStatus(status);
            if (game_pointer>=numPlayers-1){
                while (judger.judge_round1(dealer.getHand())<17){
                    dealer.dealCard(dealer.getHand());
                }
                int dealScore = judger.judge_round1(dealer.getHand());
                String dealStatus = judger.judge_round2(dealer.getHand());
                getDealer().setScore(dealScore);
                getDealer().setStatus(dealStatus);
                for (int i = 0; i < numPlayers; i++) {
                    judger.judgeGame(this,i);
                }
                game_pointer = 0;
            }else {
                game_pointer += 1;
            }
        }
    }

    public Array<BlackjackPlayer> getPlayers() {
        return players;
    }

    public BlackjackDealer getDealer() {
        return dealer;
    }

    public BlackStatus getState(int playerId){
        BlackStatus status = new BlackStatus();
        status.setLegalAction(new int[]{0,1});
        Array<Card> hand = players.get(playerId).getHand();
        int userScore = judger.judge_round1(hand);
        int dealScore;
        if (isOver()){
            dealScore = judger.judge_round1(dealer.getHand());
        }else {
            Array<Card> array = new Array<>(dealer.getHand());
            array.removeIndex(0);
            dealScore = judger.judge_round1(array);
        }
        if (userScore<=3){
            System.out.println("0");
        }
        status.setHandCard(new int[]{userScore,dealScore});
        return status;
    }


    public boolean isOver(){
        for (int i = 0; i < numPlayers; i++) {
            if (winner.get("player"+i) == 0) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Integer> getWinner() {
        return winner;
    }

    public void showCard() {
//        System.out.println("------------info-start--------------");
//        showInfo("dealer",dealer.getHand());
//        BlackjackPlayer blackjackPlayer = getPlayers().get(game_pointer);
//        Array<Card> hand = blackjackPlayer.getHand();
//        showInfo("user"+game_pointer,hand);
//        System.out.println("-------------info-end---------------");
    }

    public void showInfo(String name,Array<Card> hand){
        System.out.println("username --> "+name);
        for (Card card : hand) {
            System.out.print(card.toString()+"  ");
        }
        System.out.println();
    }

    public void showAction(int bestAction) {
        System.out.println("------------Action-start--------------");
        System.out.println("info --> 1 : hit  0:stand");
        System.out.println("username --> "+game_pointer+" ---> "+bestAction);
        System.out.println("-------------Action-end---------------");
    }


    public void winnerInfo() {
//        System.out.println("------------winnder-info-start--------------");
//        for (String s : winner.keySet()) {
//            System.out.println("user --> "+s+":");
//            Integer integer = winner.get(s);
//            System.out.println(integer);
//        }
//        System.out.println("------------winnder-info-end--------------");
//        System.out.println();
//        System.out.println();
//        System.out.println();

    }
}
