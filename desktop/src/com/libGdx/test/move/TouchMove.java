package com.libGdx.test.move;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class TouchMove extends LibGdxTestMain {
    public static void main(String[] args) {
        TouchMove move = new TouchMove();
        move.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Group group = new Group();
        addActor(group);
        Image image = new Image(Asset.getAsset().getTexture("shuoming.png"));
        group.addActor(image);
        group.setSize(image.getWidth(),image.getHeight());
        image.setPosition(image.getWidth()/2.0f,image.getHeight()/2.0f,Align.center);
        group.setPosition(Constant.GAMEWIDTH/2.0f,Constant.GAMEHIGHT/2.f, Align.center);
        group.addListener(new ActorGestureListener(){
            private float touchDownX;
            private float touchDownY;

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                touchDownX = x;
                touchDownY = y;
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);
                image.setPosition(-touchDownX + x,-touchDownY + y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                float x1 = image.getX();
                float y1 = image.getY();
                Vector2 c = new Vector2(x1,y1);
                image.getParent().localToStageCoordinates(c);
                group.getParent().stageToLocalCoordinates(c);
                image.setPosition(0,0 );
                group.setPosition(c.x,c.y);
            }
        });
    }
}
