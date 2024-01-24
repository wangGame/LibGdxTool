package com.libGdx.test.ai.common;

public interface GameEnv {
    public int getNumPlayers();
    public int getNumActions();
    public ResetResult initGame();
    public ResetResult step(String s);
    public boolean stepBack();
    public boolean isOver();
    public int getPlayerId();
    public State getState(int playId);

}
