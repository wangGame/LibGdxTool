package com.libGdx.test.ai.bjack;

public class BlackStatus {
    private int[] legalAction; //hit 0  stand 1
    private int[] handCard;

    public int[] getLegalAction() {
        return legalAction;
    }

    public void setLegalAction(int[] legalAction) {
        this.legalAction = legalAction;
    }

    public String getHandCard() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < handCard.length; i++) {
            builder.append(handCard[i]);
        }
        return builder.toString();
    }

    public void setHandCard(int[] handCard) {
        this.handCard = handCard;
    }
}
