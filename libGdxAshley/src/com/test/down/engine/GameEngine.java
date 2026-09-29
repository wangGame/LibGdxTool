package com.test.down.engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

public class GameEngine {
    private Engine engine;
    public static GameEngine instance;
    public GameEngine(){
        instance = this;
        this.engine = new Engine();
    }

    private void initSystems(){
        engine.addSystem(new EntitySystem() {
            @Override
            public void addedToEngine(Engine engine) {
                super.addedToEngine(engine);
            }
        });
    }
}
