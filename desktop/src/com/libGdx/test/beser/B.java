package com.libGdx.test.beser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;


public class B extends LibGdxTestMain {
    private ShapeRenderer renderer;
    private Vector2 lastV2 = new Vector2();
    private boolean inited = false;
    public static void main(String[] args) {
        B b = new B();
        b.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        renderer = new ShapeRenderer();
        renderer.setColor(Color.WHITE);
        Actor image = new Actor(){
            @Override
            public void setPosition(float x, float y) {
                super.setPosition(x, y);
                if (!inited){
                    inited = true;
                    lastV2.set(x,y);
                    return;
                }else {
                    renderer.begin(ShapeRenderer.ShapeType.Line);
                    renderer.line(lastV2.x, lastV2.y, x,y);
                    lastV2.set(x,y);
                    renderer.end();
                }
            }
        };
        addActor(image);
        Array<Vector2> a = new Array<>();
        a.add(new Vector2(0, 0));
        a.add(new Vector2(0, 0));
        a.add(new Vector2(300, 0));
        a.add(new Vector2(600, 900));
        a.add(new Vector2(400, 0));
        a.add(new Vector2(600, 0));
        a.add(new Vector2(100, 0));
        a.add(new Vector2(10, 1200));
        a.add(new Vector2(700, 0));
        a.add(new Vector2(80, 200));
        BUL2 bu = new BUL2(a);
        bu.setDuration(2);
        bu.setInterpolation(Interpolation.sineOut);
        image.addAction(bu);
    }
}
