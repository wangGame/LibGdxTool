package com.libGdx.test.fsm;

public class Player {
    private StateMachine stateMachine;
    public Player(){
        this.stateMachine = new StateMachine();
        this.stateMachine.setParams(Direction.UP,true);
    }

    public void udpateDir(Direction direction){
        this.stateMachine.setParams(direction,true);
    }
}
