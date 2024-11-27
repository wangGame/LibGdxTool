package com.libGdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.camera.DemoCamera;

public class CameraDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        run(CameraDemo.class);
    }

    ShapeRenderer renderer;
    DemoCamera demoCamera;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        renderer = new ShapeRenderer();
        demoCamera = new DemoCamera();
        // Tell LibGDX that demoCamera knows what to do with keypresses
        Gdx.input.setInputProcessor(demoCamera);
    }

    @Override
    public void resize(int width, int height) {
        demoCamera.resize(width, height);
    }

}
