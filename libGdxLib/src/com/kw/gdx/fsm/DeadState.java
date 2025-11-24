package com.kw.gdx.fsm;

class DeadState implements State<Player> {
    @Override
    public void onEnter(Player owner, Object... args) {
        System.out.println("Player → Dead");
    }
    @Override
    public void onUpdate(Player owner, float delta) { }
    @Override
    public void onExit(Player owner) { }
}
