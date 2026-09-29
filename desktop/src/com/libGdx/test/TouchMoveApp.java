package com.libGdx.test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.libGdx.test.base.LibGdxTestMain;

public class TouchMoveApp extends LibGdxTestMain {
    public static void main(String[] args) {
        TouchMoveApp touchMoveApp = new TouchMoveApp();
        touchMoveApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);


        Group group = new Group();
        group.setSize(1200, 1200);
        group.setDebug(true);
        addActor(group);
        Image btn = new Image(Asset.getAsset().getTexture("assets/0_1_41_512.jpg"));
        group.addActor(btn);
        group.setPosition(100,200);

        group.addListener(new InputListener() {
            float offsetX;
            float offsetY;
            Vector2 vector2 = new Vector2();
            @Override
            public boolean touchDown(
                    InputEvent event,
                    float x,
                    float y,
                    int pointer,
                    int button) {

                vector2.set(x, y);
                group.localToStageCoordinates(vector2);
                btn.stageToLocalCoordinates(vector2);
                offsetX= vector2.x;
                offsetY= vector2.y;

                return true;
            }

            @Override
            public void touchDragged(
                    InputEvent event,
                    float x,
                    float y,
                    int pointer) {


                float targetX = x ;
                float targetY = y ;


                btn.setPosition(targetX - offsetX, targetY-offsetY);
            }
        });
    }
}
