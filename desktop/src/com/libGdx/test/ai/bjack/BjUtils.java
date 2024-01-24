package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.utils.Array;

public class BjUtils {
    public static Array<Card> initDeck(){
        Array<Card> cards = new Array<>();
        String [] suit_list = {"S", "H", "D", "C"};
        String [] rank_list = {"A", "2", "3", "4", "5", "6", "7",
                "8", "9", "T", "J", "Q", "K"};
        for (String s : suit_list) {
            for (String s1 : rank_list) {
                cards.add(new Card(s,s1));
            }
        }
        return cards;
    }

}
