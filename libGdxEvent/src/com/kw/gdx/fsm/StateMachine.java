package com.kw.gdx.fsm;

import java.util.Map;

public abstract class StateMachine {
    protected State currentState;
    protected Map<String,State> stateMachines;
}
