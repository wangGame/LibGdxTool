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
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;

public class MyGdxGame2 extends ApplicationAdapter {
    public Environment environment;//可以包含点光源集合和线光源集合
    public OrthographicCamera cam;//3D视角
    public CameraInputController camController;//视角控制器
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelBatch modelBatch;
    public boolean loading;
    private ModelInstance shipInstance;
    private AssetManager assets;
    private ModelInstance tableModelInstance;
    @Override
    public void create () {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));//环境光
        PointLight set = new PointLight();
        set.set(1.0f, 1.f, 1.f, .0f, .0f, 30, 1000.0f);
//        environment.add(set);
        environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, 0f, 0, -1));

        modelBatch = new ModelBatch();
        cam = new OrthographicCamera(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//67可以理解成一个定值，视角宽度（67度）
        cam.position.set(0f, 0f, 180f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 1300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        loading = true;
        doneLoading();
        assets = new AssetManager();
        assets.setLoader(Model.class, ".g3db", new G3dModelLoader(new UBJsonReader(),assets.getFileHandleResolver()));
        assets.setLoader(Model.class, ".obj", new ObjLoader(assets.getFileHandleResolver()));
        assets.load("model/table.g3db", Model.class);
        assets.load("wood.png",Texture.class);
        assets.finishLoading();
        Model tableModel = assets.get("model/table.g3db", Model.class);
        tableModelInstance = new ModelInstance(tableModel);
        tableModelInstance.transform = new Matrix4()
                .scale(500.0f, 5000.0f, 500.0f);
        tableModelInstance.transform.translate(new Vector3(0,0,-1));
        Texture woodTexture = assets.get("wood.png", Texture.class);
        woodTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        woodTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tableModelInstance.getMaterial("Table").set(
                TextureAttribute.createDiffuse(woodTexture)
        );
        tableModelInstance.transform.rotate(new Vector3(1,0,0),90);
    }


    private void doneLoading() {

        Model modelUp = new ObjLoader().loadModel(Gdx.files.internal("model/mode.g3db"));
        Texture texture = new Texture(Gdx.files.internal("textures/d1.png"));
        TextureAttribute diffuse = TextureAttribute.createDiffuse(texture);
        Material material1 = new Material(
                diffuse);
        Material material2 = new Material(
                TextureAttribute.createDiffuse(texture));
        shipInstance = new ModelInstance(modelUp);
        shipInstance.transform.translate(0,0,0);
        // 遍历并为所有Node的NodePart应用材质
        Node node1 = shipInstance.nodes.get(0);

        material1.set(
                ColorAttribute.createDiffuse(1f, 1f, 1f, 1.0f),
                ColorAttribute.createSpecular(0.5f, 0.5f, .5f, 1.0f),
                FloatAttribute.createShininess(1000.0f)
        );
        material2.set(
                ColorAttribute.createDiffuse(0.09f, 0.09f, 0.07f, 1.0f),
                ColorAttribute.createSpecular(1.0f, 1.0f, 1.0f, 1.0f),
                FloatAttribute.createShininess(0.0f)
        );


        node1.parts.get(0).material = material1;
        Node node2 = shipInstance.nodes.get(1);


        node2.parts.get(0).material = material2;
        instances.add(shipInstance);
        loading = false;

        shipInstance.transform.scale(1000,1000,1000);
        shipInstance.nodes.get(0).scale.set(new Vector3(3,6,1));
        shipInstance.nodes.get(1).scale.set(new Vector3(3,6,1));
        shipInstance.calculateTransforms();
        shipInstance.transform.rotate(new Vector3(0,1,0),10);
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
        modelBatch.render(tableModelInstance,environment);
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
//        Gdx.isJiami = true;
        new LwjglApplication(new MyGdxGame2(), config);
//        int index = 0;
//        for (int i = 0; i <= 6; i++) {
//            for (int i1 = i; i1 <= 6; i1++) {
//                System.out.println(i+ "  " +i1);
//                index++;
//            }
//        }
//        System.out.println(index);
    }
}