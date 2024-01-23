package com.libGdx.test.ai.bjack;

public class Card {
    public int index = 0;

    public Card(int i) {
        this.index = i;
        if (index>11){
            index = 10;
        }
    }
}
