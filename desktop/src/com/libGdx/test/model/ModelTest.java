package com.libGdx.test.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.ModelActor;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class ModelTest extends LibGdxTestMain {
    private ModelActor modelActor;
    private Vector3 pos = new Vector3();
    public static void main(String[] args) {
        ModelTest modelTest = new ModelTest();
        modelTest.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Constant.viewColor = new Color(49/255.0f,77/255.0f,121/255.0f,1.f/255.0f);
        this.modelActor = new ModelActor(ModelUtils.createInstance(0));
        addActor(modelActor);
        modelActor.setPosition(new Vector3(100,500,-400));
        Vector3 rotation = new Vector3();
        rotation.set(3,0,0);
        modelActor.rotation(rotation);
        modelActor.setScale(new Vector3(2,2,6));
        modelActor.setDebug(true);
    }


    @Override
    public void render() {
        super.render();
    }
}
