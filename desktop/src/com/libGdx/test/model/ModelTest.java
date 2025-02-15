package com.libGdx.test.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;

public class ModelTest extends LibGdxTestMain {
    private ThreeActor actor;
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
                System.out.println("==================");
                actor.mmx();

            }
        });
        actor = new ThreeActor();
        addActor(actor);
        Image image1 = new Image(texture);
//        addActor(image1);
        image1.setPosition(500,100);
        actor.setPosition(10,10);

    }
}
