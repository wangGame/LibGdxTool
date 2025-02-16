package com.libGdx.test.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.model.g3.GameObject;

public class ModelTest extends LibGdxTestMain {
//    private ThreeActor actor;
    public static void main(String[] args) {
        ModelTest modelTest = new ModelTest();
        modelTest.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Texture texture = new Texture(Gdx.files.internal("0_1_41_512.jpg"));
        Image image = new Image(texture);
        addActor(image);
        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });


//        for (int i = 1; i < 2; i++) {
        tileGameObject = new TileGameObject(){
            @Override
            public void act(float delta) {
                super.act(delta);
            }
        };
        tileGameObject.moveTo(new Vector3(180,500,-300));
        addActor(tileGameObject);
        tileGameObject.addListener(new ActorGestureListener(){
            private float speed;
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);
                tileGameObject.rotate(deltaX,-deltaY,0);
//                tileGameObject.rotate(new Vector3(1,0,0),deltaX);
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                super.fling(event, velocityX, velocityY, button);

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                touchx = true;
            }
        });
//        }

        Image image1 = new Image(texture);
        image1.setPosition(500,100);
        addActor(image1);
    }

    private TileGameObject tileGameObject;
    private boolean touchx = false;
}
