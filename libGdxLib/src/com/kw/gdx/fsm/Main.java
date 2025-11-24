package com.kw.gdx.fsm;

public class Main {
    public static void main(String[] args) {
        Player p = new Player();
        StateMachine<Player> fsm = new StateMachine<>(p);

        fsm.addState(new IdleState());
        fsm.addState(new RunState());
        fsm.addState(new DeadState());

        fsm.changeState(IdleState.class);
        fsm.changeState(RunState.class, 6.5f);
        fsm.changeState(DeadState.class);
    }
}
