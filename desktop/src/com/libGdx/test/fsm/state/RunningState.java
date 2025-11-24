package com.libGdx.test.fsm.state;

import com.kw.gdx.fsm.State;
import com.libGdx.test.fsm.Player;


// 跑动状态
class RunningState implements State<Player> {

    @Override
    public void onEnter(Player owner, Object... args) {
        System.out.println(owner.name + " starts Running");
    }

    @Override
    public void onUpdate(Player owner, float delta) {
        System.out.println(owner.name + " stops Running");
    }

    @Override
    public void onExit(Player owner) {

    }

    @Override
    public void handleEvent(Player player, String event) {
        if ("stop".equals(event)) {

        }
    }
}