package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class BlackjackPlayer {
    private Random random;
    private int userID;
    private Array<Card> hand;
    private String status;
    private int score;

    public BlackjackPlayer(int playId,Random random){
        this.userID = playId;
        this.random = random;
        this.hand = new Array<>();
        this.status = "alive";
        this.score = 0;
    }

    public int getUserID() {
        return userID;
    }

    public Array<Card> getHand() {
        return hand;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }
}
