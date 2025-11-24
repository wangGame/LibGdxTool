package com.libGdx.test.fsm;

import com.kw.gdx.fsm.StateMachine;

public class Player {
    public String name;
    public StateMachine<Player> stateMachine;

    public Player(String name) {
        this.name = name;
        this.stateMachine = new StateMachine<>(this);
    }

    public void update(float delta) {
        stateMachine.update(delta);
    }

    public void handleEvent(String event) {
        stateMachine.handleEvent(event);
    }
}