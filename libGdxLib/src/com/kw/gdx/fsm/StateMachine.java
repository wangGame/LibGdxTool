package com.kw.gdx.fsm;

public class StateMachine<T> {
    private T owner;
    private State<T> currentState;

    public StateMachine(T owner) {
        this.owner = owner;
    }

    public void changeState(State<T> newState) {
        if (currentState != null) {
            currentState.exit(owner);
        }
        currentState = newState;
        if (currentState != null) {
            currentState.enter(owner);
        }
    }

    public void update(float delta) {
        if (currentState != null) {
            currentState.update(owner, delta);
        }
    }

    public void handleEvent(String event) {
        if (currentState != null) {
            currentState.handleEvent(owner, event);
        }
    }

    public State<T> getCurrentState() {
        return currentState;
    }
}
