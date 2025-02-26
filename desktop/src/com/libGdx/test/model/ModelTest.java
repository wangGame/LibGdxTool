package com.libGdx.test.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BaseJsonReader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.kw.gdx.ModelActor;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.model.g3.GameObject;

public class ModelTest extends LibGdxTestMain {
    private ModelActor modelActor;
    private Vector3 pos = new Vector3();
    public static void main(String[] args) {
        ModelTest modelTest = new ModelTest();
        modelTest.start();
    }
//    convertedModel.g3db
    private Model mModel;
    public void useShow(Stage stage) {
        super.useShow(stage);

         mModel = new G3dModelLoader(new UBJsonReader()).loadModel(Gdx.files.internal("convertedModel.g3db"));

//        Model modelUp = new G3dModelLoader(new BaseJsonReader).loadModel(Gdx.files.internal("model/Cube_0.obj"));

        Constant.viewColor = new Color(49/255.0f,77/255.0f,121/255.0f,1.f/255.0f);
        ModelUtils.createInstance();
        this.modelActor = new ModelActor(new ModelInstance(mModel));
        addActor(modelActor);
        modelActor.setPosition(new Vector3(500,500,-400));
        Vector3 rotation = new Vector3();
        rotation.set(3,0,0);
        modelActor.rotation(rotation);
        modelActor.setScale(new Vector3(200,200,60));
        modelActor.setDebug(true);
    }


    @Override
    public void render() {
        super.render();
    }
}
