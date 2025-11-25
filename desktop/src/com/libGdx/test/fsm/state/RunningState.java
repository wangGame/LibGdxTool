package com.libGdx.test.fsm.state;

import com.kw.gdx.fsm.State;
import com.libGdx.test.fsm.Player;

// 跑动状态


public class RunningState implements State<Player> {
    @Override
    public void enter(Player p) {
        System.out.println(p.name + " 开始 Running");
    }

    @Override
    public void exit(Player p) {
        System.out.println(p.name + " 停止 Running");
    }

    @Override
    public void update(Player p, float delta) {
        // 跑步逻辑
    }

    @Override
    public void handleEvent(Player p, String event) {
        if (event.equals("idle")) {
            p.stateMachine.changeState(new IdleState());
        }
    }
}
