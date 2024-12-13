package com.libGdx.test.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class SpriteApp extends LibGdxTestMain {
    public static void main(String[] args) {
        SpriteApp spriteApp = new SpriteApp();
        spriteApp.start();
    }

    private EntityApp app;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        SpriteGroup spriteGroup= new SpriteGroup();
//        addActor(spriteGroup);
//
        app = new EntityApp((short) 100,100,100);
        Actor actor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                app.render(Gdx.graphics.getDeltaTime(),batch);
            }
        };
        addActor(actor);
    }

    @Override
    public void render() {
        super.render();
    }
}
