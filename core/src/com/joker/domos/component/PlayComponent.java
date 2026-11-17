package com.joker.domos.component;

import com.badlogic.ashley.core.Component;

public class PlayComponent implements Component {
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
