package com.libGdx.test.other;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class Demo01 extends LibGdxTestMain {
    public static void main(String[] args) {
        Demo01 d = new Demo01();
        d.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ShapeRenderer renderer = new ShapeRenderer();
        Group group = new Group(){
            @Override
            public void act(float delta) {
                super.act(delta);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                batch.end();
                renderer.setProjectionMatrix(batch.getProjectionMatrix());
                renderer.setTransformMatrix(batch.getTransformMatrix());
                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.rect(0,0,200,800);
                renderer.end();
                batch.begin();
            }
        };

        addActor(group);
    }

    @Override
    public void render() {
        super.render();
    }
}
