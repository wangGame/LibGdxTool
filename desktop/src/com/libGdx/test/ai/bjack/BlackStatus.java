package com.libGdx.test.ai.bjack;

public class BlackStatus {
    public int[] legalAction; //hit 0  stand 1
    public double[] handCard;

    public int[] getLegalAction() {
        return legalAction;
    }

    public void setLegalAction(int[] legalAction) {
        this.legalAction = legalAction;
    }

    public double[] getHandCard() {
        return handCard;
    }

    public void setHandCard(double[] handCard) {
        this.handCard = handCard;
    }
}
