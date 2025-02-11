package com.libGdx.test.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;

public class ModelTest extends LibGdxTestMain {
    public Environment environment;//可以包含点光源集合和线光源集合
    public PerspectiveCamera cam;//3D视角
    public CameraInputController camController;//视角控制器


    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelBatch modelBatch;

    public boolean loading;

    public static void main(String[] args) {
        ModelTest modelTest = new ModelTest();
        modelTest.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
    }
}
