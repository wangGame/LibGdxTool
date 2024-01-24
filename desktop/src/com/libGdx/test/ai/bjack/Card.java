package com.libGdx.test.ai.bjack;

import java.util.Arrays;
import java.util.Objects;

public class Card {
    private String suit;
    private String rank;
    private String valid_suit[] = {"S", "H", "D", "C", "BJ", "RJ"};
    private String []valid_rank = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"};

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(suit, card.suit) && Objects.equals(rank, card.rank) && Arrays.equals(valid_suit, card.valid_suit) && Arrays.equals(valid_rank, card.valid_rank);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(suit, rank);
        result = 31 * result + Arrays.hashCode(valid_suit);
        result = 31 * result + Arrays.hashCode(valid_rank);
        return result;
    }

    @Override
    public String toString() {
        return rank + suit;
    }


    public String get_index() {
        return  suit+rank;
    }
}
