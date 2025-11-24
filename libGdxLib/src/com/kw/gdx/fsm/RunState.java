package com.kw.gdx.fsm;

class RunState implements State<Player> {
    private float speed;

    @Override
    public void onEnter(Player owner, Object... args) {
        speed = (float) args[0];
        System.out.println("Player → Run, speed = " + speed);
    }
    @Override
    public void onUpdate(Player owner, float delta) {
        // update movement
    }
    @Override
    public void onExit(Player owner) {
        System.out.println("Exit Run");
    }
}
