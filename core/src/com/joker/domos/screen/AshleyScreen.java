package com.joker.domos.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.joker.domos.ashley.PrintSystem;
import com.joker.domos.component.PlayComponent;
import com.joker.domos.component.TestComponent;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;

public class AshleyScreen extends BaseScreen {
    private Engine engine;
    public AshleyScreen(BaseGame game) {
        super(game);
        initEngine();
    }

    public void initEngine(){
        engine = new Engine();
        engine.addSystem(new PrintSystem());
        Entity playEntity = new Entity();
        PlayComponent playComponent = new PlayComponent();
        playComponent.setType(1);
        playEntity.add(playComponent);
        engine.addEntity(playEntity);

        Entity testEntity = new Entity();
        TestComponent testComponent = new TestComponent();
        testEntity.add(testComponent);
        engine.addEntity(testEntity);

        Image image=  new Image(Asset.getAsset().getTexture("000.png"));
        addActor(image);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        super.render(delta);
    }
}
