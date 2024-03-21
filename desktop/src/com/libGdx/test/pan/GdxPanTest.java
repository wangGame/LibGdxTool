package com.libGdx.test.pan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class GdxPanTest extends LibGdxTestMain {
    public static void main(String[] args) {
        GdxPanTest test = new GdxPanTest();
        test.start();
    }
    private Image testImg;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        this.testImg = new Image(Asset.getAsset().getTexture("ball.png"));
        testImg.setColor(Color.BLACK);
        addActor(testImg);
        stage.addCaptureListener(new ActorGestureListener(){
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);
                testImg.setPosition(x,y);
            }
        });
    }
}
