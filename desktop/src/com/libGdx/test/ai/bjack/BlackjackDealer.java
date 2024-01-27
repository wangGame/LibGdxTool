package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class BlackjackDealer {
    private Random random;
    private int numDecks;
    private Array<Card> deck;
    private Array<Card> hand;
    private String status;
    private int score;
    public BlackjackDealer(Random random,int decks){
        this.random = random;
        this.numDecks = decks;
        this.deck = BjUtils.initDeck();
        this.deck.shuffle();
        this.hand = new Array<>();
        this.status = "alive";
        this.score = 0;
    }

    public void dealCard(Array<Card> hand){
        int i = random.nextInt(deck.size);
        Card card = deck.get(i);
        hand.add(card);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Array<Card> getHand() {
        return hand;
    }

    public String getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }
}
