package com.libGdx.test.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;

public class ModelExample extends ApplicationAdapter {
    public Environment environment;//可以包含点光源集合和线光源集合
    public OrthographicCamera cam;//3D视角
    public CameraInputController camController;//视角控制器
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelBatch modelBatch;
    public boolean loading;
    private ModelInstance shipInstance;

    @Override
    public void create () {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.9f, 0.9f, 0.9f, 1f));//环境光
        modelBatch = new ModelBatch();
        cam = new OrthographicCamera(1, 1);//67可以理解成一个定值，视角宽度（67度）
        cam.position.set(0f, 0f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 0f;
        cam.far = 100f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
        loading = true;
        doneLoading();
    }


    private void doneLoading() {
        Model modelUp = new ObjLoader().loadModel(Gdx.files.internal("model/Cube_0.obj"));
        Texture texture = new Texture(Gdx.files.internal("textures/1.png"));
        TextureAttribute diffuse = TextureAttribute.createDiffuse(texture);
        Material material1 = new Material(
                diffuse);
        Material material2 = new Material(
                TextureAttribute.createDiffuse(texture));
        shipInstance = new ModelInstance(modelUp);
        // 遍历并为所有Node的NodePart应用材质
        Node node1 = shipInstance.nodes.get(0);
        node1.parts.get(0).material = material1;
        Node node2 = shipInstance.nodes.get(1);
        node2.parts.get(0).material = material2;
        instances.add(shipInstance);
        loading = false;
//        shipInstance.transform.scale(1,1,1);
//        shipInstance.nodes.get(0).scale.set(new Vector3(3,6,1));
//        shipInstance.nodes.get(1).scale.set(new Vector3(3,6,1));
//        shipInstance.calculateTransforms();

//
//        Vector3 vector3 = new Vector3();
//        shipInstance.transform.getTranslation(vector3);
//        shipInstance.transform.

//        ModelActor modelActor = new ModelActor();

    }

    @Override
    public void render () {
        super.render();
        camController.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//        shipInstance.transform.rotate(new Vector3(1,0,0), (float) Math.toRadians(30));
        modelBatch.begin(cam);
        modelBatch.render(instances,environment);
        modelBatch.end();
//
//        float accelerometerX = Gdx.input.getAccelerometerX();
//        float accelerometerY = Gdx.input.getAccelerometerY();
//        float accelerometerZ = Gdx.input.getAccelerometerZ();

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
        new LwjglApplication(new ModelExample(), config);
    }
}