//package com.libGdx.test.ai.bjack;
//
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.utils.Array;
//import com.kw.gdx.asset.Asset;
//import com.kw.gdx.constant.Constant;
//
//import java.util.HashMap;
//
//public class BlackJackGroup extends Group {
//    private HashMap<String,Integer> stateMap = new HashMap<>();
//    private double[][] rwardBlack;
//    private HashMap<Integer,Array<Card>> userCards;
//    public BlackJackGroup(){
//        userCards = new HashMap<>();
//        userCards.put(0,new Array<>()); //玩家
//        userCards.put(1,new Array<>()); //house
//        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
//        Image bg = new Image(Asset.getAsset().getTexture(""));
//        addActor(bg);
//    }
//
//    private Array<Card> cards = new Array<>();
//    /**
//     * 发牌
//     */
//    public void initGame(){
//        for (int i = 1; i <= 13; i++) {
//            cards.add(new Card(i));
//        }
//    }
//
//    public void shuffle(){
//        cards.shuffle();
//    }
//
//    public void dealPack(){
//        Card card = cards.removeIndex(cards.size - 1);
//        for (int j = 0; j < 2; j++) {
//            for (int i = 0; i < 2; i++) {
//                userCards.get(i).add(card);
//            }
//        }
//    }
//
//    public int getScore(int user){
//        Array<Card> card = userCards.get(user);
//        int counta = 0;
//        int score = 0;
//        for (Card card1 : card) {
//            int index = card1.index;
//            if (index==1){
//                score += 11;
//                counta++;
//            }else {
//                score += index;
//            }
//        }
//        while (score>21 && counta>0){
//            counta--;
//            score -= 10;
//        }
//        return score;
//    }
//
//    public void train(){
//        String state = getStateMap();
//        String stateNew = getStateMap();
//        int stateQ = stateMap.getOrDefault(state, 0);
//        int stateNewQ = stateMap.getOrDefault(stateNew, 0);
////        int new_q = stateQ + alpha * (reward + gamma * stateNewQ - stateQ);
//        stateMap.put(stateNew,new_q);
//    }
//
//    private int bet = 10;
//    public int busted(int userId){
//        if (userId == 1) {
//            return bet;
//        }else {
//            return -bet;
//        }
//    }
//
//    public int backjack(int userId){
//        if (userId == 1) {
//            return bet;
//        }else {
//            return -bet * 2;
//        }
//    }
//
////    public int DOUBLE = 0;
//    public int HIT = 0;
////    public int SPLIT = 2;
//    public int STAND = 1;
//
//    private int currentAction;
//
//    public String getStateMap(){
//        String cardsString="";
//        Array<Card> cards = userCards.get(0);
//        for (Card card : cards) {
//            cardsString+=card;
//        }
//        Array<Card> cards1 = userCards.get(1);
//        cardsString+="-";
//        cardsString+=cards1.get(0)+"-"+currentAction;
//        return cardsString;
//    }
//}
