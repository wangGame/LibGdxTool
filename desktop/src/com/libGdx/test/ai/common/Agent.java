package com.libGdx.test.ai.common;

public interface Agent {
    public boolean useRaw();
    public String eval_step(State state);

    String step(State state);
}
