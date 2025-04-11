package com.libGdx.test.beser;

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
    public static void main(String[] args) {
        B b = new B();
        b.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Actor image = new Actor(){
            @Override
            public void setPosition(float x, float y) {
                super.setPosition(x, y);
                Image t = new Image(Asset.getAsset().getTexture("assets/white.png"));
                addActor(t);
                t.setPosition(x,y, Align.center);
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



        BUL1 bu = new BUL1(a);
        bu.setDuration(2);
        bu.setInterpolation(Interpolation.sineOut);
        image.addAction(bu);
    }
}
