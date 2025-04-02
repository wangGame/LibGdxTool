package com.libGdx.test.beser;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
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
        Image image = new Image(Asset.getAsset().getTexture("assets/7.png")){
            @Override
            public void setPosition(float x, float y) {
                super.setPosition(x, y);
                Image t = new Image(Asset.getAsset().getTexture("assets/white.png"));
                addActor(t);
                t.setPosition(x,y, Align.center);
            }
        };
        addActor(image);
        BUL bu = new BUL(new Vector2(0, 0), new Vector2(0, 0), new Vector2(300, 0), new Vector2(300, 600));
        bu.setDuration(5);
        image.addAction(bu);
    }
}
