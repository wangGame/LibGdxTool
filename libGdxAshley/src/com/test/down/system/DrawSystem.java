package com.test.down.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class DrawSystem extends IteratingSystem {
    public DrawSystem(Family family) {
        super(family);
    }

    public DrawSystem(Family family, int priority) {
        super(family, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
