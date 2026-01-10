package com.libGdx.test.event;

public class PlatformRequest {
    public enum Type { SHOW_REWARD_AD }
    public final int requestId;
    public final Type type;

    public PlatformRequest(int requestId, Type type) {
        this.requestId = requestId;
        this.type = type;
    }
}
