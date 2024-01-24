package com.libGdx.test.ai.bjack;

import com.badlogic.gdx.utils.Array;
import com.libGdx.test.ai.common.Env;
import com.libGdx.test.ai.dqn.Agent.DQN;
import com.libGdx.test.ai.path.GameEnv;

public class BjEnv extends Env {
    private String name;
    private GameEnv game;
    private Array<String> actions;
    public BjEnv(){

    }
}
