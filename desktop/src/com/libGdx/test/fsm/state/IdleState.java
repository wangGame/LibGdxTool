package com.libGdx.test.fsm.state;

import com.kw.gdx.fsm.State;
import com.libGdx.test.fsm.Player;

// 空闲状态
public class IdleState implements State<Player> {
    @Override
    public void onEnter(Player owner, Object... args) {
        System.out.println(owner.name + " enters Idle");
    }

    @Override
    public void onUpdate(Player owner, float delta) {

    }

    @Override
    public void onExit(Player owner) {
        System.out.println(owner.name + " exits Idle");
    }

    @Override
    public void handleEvent(Player player, String event) {
        if ("run".equals(event)) {
            player.stateMachine.changeState(new RunningState(),"");
        }
    }
}
