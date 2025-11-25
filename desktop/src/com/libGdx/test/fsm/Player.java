package com.libGdx.test.fsm;

import com.kw.gdx.fsm.Entity;
import com.kw.gdx.fsm.StateMachine;


public class Player extends Entity<Player> {
    public String name;

    public Player(String name) {
        this.name = name;
        this.stateMachine = new StateMachine<>(this);
    }

    @Override
    public void update(float delta) {
        stateMachine.update(delta);
    }

    @Override
    public void handleEvent(String event) {
        stateMachine.handleEvent(event);
    }
}
