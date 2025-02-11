package com.libGdx.test.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;

public class MyGdxGame2 extends ApplicationAdapter {
    public Environment environment;//可以包含点光源集合和线光源集合
    public OrthographicCamera cam;//3D视角
    public CameraInputController camController;//视角控制器

    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelBatch modelBatch;

    public boolean loading;

    private Texture texture = new Texture(Gdx.files.internal("path/to/your/texture.png"));


    @Override
    public void create () {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));//环境光
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));//直线光源

        modelBatch = new ModelBatch();
        cam = new OrthographicCamera(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//67可以理解成一个定值，视角宽度（67度）
        cam.position.set(10f, 10f, -70f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 1300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        loading = true;
        doneLoading();
    }

    private void doneLoading() {
        Model model = new ObjLoader().loadModel(Gdx.files.internal("model/Cube1.obj"));
//        Model ship = new Model("model/invaderscene.g3db", Model.class);
        ModelInstance shipInstance = new ModelInstance(model);
        instances.add(shipInstance);
        loading = false;
        shipInstance.transform.scale(3000,6000,1000);

    }

    @Override
    public void render () {


        camController.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances,environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();

        super.dispose();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1920 * 0.25f);
        config.width = (int) (1080 * 0.5f);
        Gdx.isJiami = true;
        new LwjglApplication(new MyGdxGame2(), config);
    }
}