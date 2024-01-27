package com.libGdx.test.ai.feii.common;

public class ResetResult {
    private State state;
    private int playerId;

    public void setState(State state) {
        this.state = state;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public State getState() {
        return state;
    }

    public int getPlayerId() {
        return playerId;
    }
}

