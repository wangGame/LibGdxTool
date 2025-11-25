package com.libGdx.test.fsm.state;

import com.kw.gdx.fsm.State;
import com.libGdx.test.fsm.Player;

public class IdleState extends State<Player> {


    @Override
    public void enter(Player p) {
        System.out.println(p.name + " 进入 Idle 状态");
    }

    @Override
    public void exit(Player p) {
        System.out.println(p.name + " 离开 Idle 状态");
    }

    @Override
    public void update(Player p, float delta) {
        // 空闲时做的事情
    }

    public String getStateName() {
        return stateName;
    }

    @Override
    public void handleEvent(Player p, String event) {

    }
}
