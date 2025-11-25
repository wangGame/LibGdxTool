package com.libGdx.test.fsm;

import com.badlogic.gdx.utils.ArrayMap;
import com.kw.gdx.fsm.Entity;
import com.kw.gdx.fsm.State;
import com.kw.gdx.fsm.StateMachine;


public class Player extends Entity<Player> {
    public String name;

    public Player(String name) {
        super();
        this.name = name;
        this.stateMachine = new StateMachine<>(this);
        this.stateArrayMap = new ArrayMap<>();
    }

    public void addState(State state){
        this.stateArrayMap.put(state.stateName,state);
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
