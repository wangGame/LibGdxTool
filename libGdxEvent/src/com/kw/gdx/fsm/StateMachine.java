package com.kw.gdx.fsm;

public class StateMachine<T extends Entity> {
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
        if (currentState!=null&&currentState.stateName.equals(event)){
            System.out.println("运行中 ------->"+event);
            return;
        }
        State o = (State) owner.stateArrayMap.get(event);
        owner.stateMachine.changeState(o);
    }

    public State<T> getCurrentState() {
        return currentState;
    }
}
