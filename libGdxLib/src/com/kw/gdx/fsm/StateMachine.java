package com.kw.gdx.fsm;

import java.util.HashMap;
import java.util.Map;

public class StateMachine<T> {
    private final T owner;
    private final Map<String, State<T>> stateMap = new HashMap<>();
    private State<T> currentState;
    private boolean paused = false;

    public StateMachine(T owner) {
        this.owner = owner;
    }

    public <S extends State<T>> void addState(S state) {
        String key = state.getClass().getName();
        if (stateMap.containsKey(key)) {
            throw new IllegalStateException("State already added: " + key);
        }
        stateMap.put(key, state);
    }

    public <S extends State<T>> void changeState(Class<S> stateClass, Object... args) {
        if (paused) return;
        State<T> newState = stateMap.get(stateClass);
        if (newState == null) throw new IllegalArgumentException("State not found: " + stateClass.getSimpleName());

        if (currentState != null) currentState.onExit(owner);
        currentState = newState;
        currentState.onEnter(owner, args);
    }

    public void update(float delta) {
        if (!paused && currentState != null) currentState.onUpdate(owner, delta);
    }

    public void pause() { paused = true; }
    public void resume() { paused = false; }
    public State<T> getCurrentState() { return currentState; }
}
