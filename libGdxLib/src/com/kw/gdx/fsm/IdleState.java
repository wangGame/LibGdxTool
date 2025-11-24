package com.kw.gdx.fsm;

class IdleState implements State<Player> {
    @Override
    public void onEnter(Player owner, Object... args) {
        System.out.println("Player → Idle");
    }
    @Override
    public void onUpdate(Player owner, float delta) {
        // idle logic
    }
    @Override
    public void onExit(Player owner) {
        System.out.println("Exit Idle");
    }
}
