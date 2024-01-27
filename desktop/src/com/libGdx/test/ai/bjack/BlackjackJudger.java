package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;

public class BlackjackJudger {
    private Random random;
    private HashMap<String,Integer> rankScore;
    private int score;
    private String status;
    public BlackjackJudger(Random random){
        rankScore = new HashMap();
        rankScore.put("A",11);
        rankScore.put("2",2);
        rankScore.put("3",3);
        rankScore.put( "4",4);
        rankScore.put( "5",5);
        rankScore.put( "6",6);
        rankScore.put( "7",7);
        rankScore.put( "8",8);
        rankScore.put( "9",9);
        rankScore.put("T",10);
        rankScore.put("J",10);
        rankScore.put("Q",10);
        rankScore.put("K",10);
        this.random = random;
    }

    public int judge_round1(Array<Card> hand){
        int score = judge_score(hand);
        if (score <= 21) {
            return score;
        } else{
            return score;
        }
    }

    public String judge_round2(Array<Card> hand){
        int score = judge_score(hand);
        if (score <= 21)
            return "alive";
        else {
            return "bust";
        }
    }

    public int judge_score(Array<Card> cards){
        int score = 0;
        int count_a = 0;
        for (Card card : cards) {
            score += rankScore.get(card.getRank());
            if (card.getRank().equals("A")){
                count_a+=1;
            }
        }
        while (score>21 && count_a>0){
            count_a -=1;
            score -= 10;
        }
        return score;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void judgeGame(BlackJackGame blackJackGame, int game_pointer) {
        HashMap<String, Integer> winner = blackJackGame.getWinner();
        BlackjackPlayer blackjackPlayer = blackJackGame.getPlayers().get(game_pointer);
        BlackjackDealer dealer = blackJackGame.getDealer();
        if (blackjackPlayer.getStatus().equals("bust")) {
            winner.put("player"+game_pointer,-1);
        }else if (dealer.getStatus().equals("bust")){
            winner.put("player"+game_pointer,2);
        }else {
            if (blackjackPlayer.getScore() > dealer.getScore()){
                winner.put("player"+game_pointer,2);
            }else if (blackjackPlayer.getScore()<dealer.getScore()){
                winner.put("player"+game_pointer,-1);
            }else {
                winner.put("player"+game_pointer,1);
            }
        }
    }
}
