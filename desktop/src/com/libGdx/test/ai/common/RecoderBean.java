package com.libGdx.test.ai.common;

public class RecoderBean {
    private int userId;
    private int action;

    public RecoderBean(int playerId, String action) {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
