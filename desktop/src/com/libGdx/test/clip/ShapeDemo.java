package com.libGdx.test.clip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.libGdx.test.base.LibGdxTestMain;

public class ShapeDemo extends LibGdxTestMain {
    private boolean move = false;
    private float height = 200;
    private float offY = 0;
    public static void main(String[] args) {
        ShapeDemo demo = new ShapeDemo();
        demo.start();
    }

    private ShapeRenderer renderer;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        renderer = new ShapeRenderer();
        stage.addAction(Actions.delay(3,Actions.run(()->{
            move = true;
        })));
    }

    @Override
    public void render() {
        super.render();
        if (renderer == null)return;
//        if (move){
//        }else {
//            renderer.begin(ShapeRenderer.ShapeType.Filled);
//            renderer.setColor(Color.BLACK);
//            renderer.circle(0, 0, 10);
//            renderer.end();
//        }

        offY += Gdx.graphics.getDeltaTime() * 9.0f;
        if (offY>=100){
            offY = 100;
        }
        renderer.setColor(Color.BLACK);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.circle(100,100,50);
        renderer.box(50,50+offY,0,200,400-offY*2,0);
        renderer.end();
    }
}
