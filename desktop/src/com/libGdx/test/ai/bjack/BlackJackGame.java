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


    public BlackJackGame(Random random,boolean allowStepBack){
        this.allowStepBack = allowStepBack;
        this.random = random;
        this.winner = new HashMap<>();
    }

    public void configure(){

    }

    private Array<BlackjackPlayer> players;
    private BlackjackDealer dealer;
    private BlackjackJudger judger;
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
//        return self.get_state(self.game_pointer), self.game_pointer

    }

    public void step(String action){
        if (allowStepBack){
//            BlackjackPlayer blackjackPlayer = players.get(game_pointer);
//
        }


    }


    public void getSatte(int playerId){

    }

    public boolean isOver(){
        for (int i = 0; i < numPlayers; i++) {
            if (winner.get("player"+i) == 0) {
                return false;
            }
        }
        return true;
    }
}
