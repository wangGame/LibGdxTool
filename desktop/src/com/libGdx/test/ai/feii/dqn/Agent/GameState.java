package com.libGdx.test.ai.feii.dqn.Agent;

public class GameState {
    public double[] state;
    public int[] legalActionsIndex;

    public double[] getState() {
        return state;
    }

    public void setState(double[] state) {
        this.state = state;
    }

    public int[] getLegalActions() {
        return legalActionsIndex;
    }

    public void setLegalActions(int[] legalActions) {
        this.legalActionsIndex = legalActions;
    }
}
