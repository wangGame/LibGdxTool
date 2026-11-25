package com.joker.domos.ashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.joker.domos.component.PlayComponent;

public class PrintSystem extends IteratingSystem {
    public PrintSystem() {
        super(Family.all(PlayComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayComponent component = entity.getComponent(PlayComponent.class);
        if (component.getType() == 1) {
            System.out.println("Processing entity with type 1");
        }
    }
}
