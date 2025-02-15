package com.libGdx.test.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;
import com.libGdx.test.A;

public class ThreeActor extends Actor {

    private static final int MATRIX_DIRECTION_X = 8;
    private static final int MATRIX_DIRECTION_Y = 9;
    private static final int MATRIX_DIRECTION_Z = 10;
    private static final int ROOT_NODE_LOCATION = 0;
    private static final float FORWARD_DIRECTION = 0f;
    private static final float BACKWARD_DIRECTION = 180f;
    public Environment environment;//可以包含点光源集合和线光源集合
    public CameraInputController camController;//视角控制器
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelBatch modelBatch;
    public boolean loading;

    public ThreeActor(){
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.9f, 0.9f, 0.9f, 1f));//环境光
        modelBatch = new ModelBatch();
//        cam = new OrthographicCamera(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//67可以理解成一个定值，视角宽度（67度）
//        cam.position.set(0f, 0f, 170f);
//        cam.lookAt(0,0,0);
//        cam.near = 1f;
//        cam.far = 1300f;
//        cam.update();
        loading = true;
        doneLoading();

    }
    private ModelInstance shipInstance;
    private void doneLoading() {
        Model modelUp = new ObjLoader().loadModel(Gdx.files.internal("model/Cube_0.obj"));
        Texture texture = new Texture(Gdx.files.internal("textures/1.png"));
        TextureAttribute diffuse = TextureAttribute.createDiffuse(texture);
        Material material1 = new Material(
                diffuse);
        Material material2 = new Material(
                TextureAttribute.createDiffuse(texture));
        shipInstance = new ModelInstance(modelUp);
        shipInstance.transform.translate(x,y,-400);
        shipInstance.transform.translate(100,y,0);
        shipInstance.transform.translate(100,y,0);
//        addAction(new Action() {
//            @Override
//            public boolean act(float delta) {
//
//                shipInstance.transform.setToTranslation(10,10,-400);
//                return false;
//            }
//        });

        shipInstance.transform.setToTranslation(10,10,-400);



        // 遍历并为所有Node的NodePart应用材质
        Node node1 = shipInstance.nodes.get(0);
        node1.parts.get(0).material = material1;
        Node node2 = shipInstance.nodes.get(1);
        node2.parts.get(0).material = material2;
        instances.add(shipInstance);
        loading = false;
        shipInstance.transform.scale(1500, 1500, 1500f);
        for (Node node : shipInstance.nodes) {
            node.scale.set(3,6,1);
        }
        shipInstance.calculateTransforms();
    }

    public void mmx(){
        shipInstance.transform.setToTranslation(30,10,-400);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
//        shipInstance.transform.setToTranslation(2,2,-400);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

//        Vector3 vector3 = new Vector3();
//        shipInstance.transform.getTranslation(vector3);
//        System.out.println(vector3);

//        shipInstance.transform.setToTranslation(100,100,-400);
//        shipInstance.transform.getTranslation(vector3);
//        System.out.println(vector3);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.flush();
        batch.end();

        Camera cam = getStage().getCamera();
        if(camController == null){
            camController = new CameraInputController(cam);
            BaseScreen currentActiveScreen = Constant.currentActiveScreen;
            InputMultiplexer multiplexer = currentActiveScreen.getMultiplexer();
            multiplexer.addProcessor(camController);
        }

//        shipInstance.transform.translate(Gdx.graphics.getDeltaTime(),0,0);
////        shipInstance.transform.translate(1,1,0);
//        shipInstance.transform.setToTranslation(10,10,-400);
//        shipInstance.transform.rotateRad(new Vector3(1, 1, 0), (float) Math.toRadians(1));
//        shipInstance.transform.scale(1,1,0.99f);
        camController.update();
        modelBatch.begin(cam);
        modelBatch.render(instances,environment);
        modelBatch.end();
        batch.begin();
    }

    public Vector3 location() {
        return transform().getTranslation(new Vector3());
    }

    public Matrix4 transform() {
        return modelInstance().transform;
    }

    private ModelInstance modelInstance() {
        return shipInstance;
    }

    public Vector3 locationFromCenter() {
        return center().add(location());
    }

    public Vector3 center() {
        return boundingBox().getCenter(new Vector3());
    }

    public Vector3 size()
    {
        return boundingBox().getDimensions(new Vector3());
    }
    public void setRotation(Quaternion quaternion)
    {
        transform().rotate(quaternion);
    }

    public void setRotation(Vector3 angles)
    {
        setRotation(angles.x, angles.y, angles. z);
    }

    public void setSize(Vector3 size)
    {
        setScale(GdxMath.divideVector(size, originalSize()));
    }

    public Vector3 rootNodeSize()
    {
        return rootNode().calculateBoundingBox(new BoundingBox())
                .getDimensions(new Vector3());
    }

    public void rotate(float yaw, float pitch, float roll)
    {
        rotate(Vector3.Y, yaw);
        rotate(Vector3.X, pitch);
        rotate(Vector3.Z, roll);
    }

    public void rotate(Vector3 angles)
    {
        rotate(angles.x, angles.y, angles.z);
    }

    public void rotate(Vector3 axis, float amount)
    {
        transform().rotate(axis, amount);
    }

    public Vector3 rotation()
    {
        Quaternion rotation = rotationQuaternion();
        return new Vector3(rotation.getYaw(),
                rotation.getPitch(),
                rotation.getRoll());
    }

    public Quaternion rotationQuaternion()
    {
        return transform().getRotation(new Quaternion());
    }

    public Vector3 scale()
    {
        return rootNode().scale;
    }

    public void scale(float scale)
    {
        setScale(scale().scl(scale));
    }

    public void scale(Vector3 scale)
    {
        setScale(scale().scl(scale));
    }

    public void setScale(float scale)
    {
        setScale(new Vector3(scale, scale, scale));
    }

    public void setScale(Vector3 scale)
    {
        rootNode().scale.set(scale);
        calculateTransforms();
    }

    public void setRotation(float yaw, float pitch, float roll)
    {
        Vector3 location = location();
        transform().setFromEulerAngles(yaw, pitch, roll);
        moveTo(location);
    }
    public void moveTo(Vector3 location)
    {
        Quaternion rotation = rotationQuaternion();
        transform().setToTranslation(location);
        setRotation(rotation);
    }


    public Ray backwardRay()
    {
        return ray(BACKWARD_DIRECTION);
    }

    public BoundingBox boundingBox()
    {
        return modelInstance().calculateBoundingBox(new BoundingBox());
    }

    protected void calculateTransforms()
    {
        modelInstance().calculateTransforms();
    }

    public Vector3 center()
    {
        return boundingBox().getCenter(new Vector3());
    }

    public float diameter()
    {
        return size().len();
    }

    public Vector3 direction()
    {
        float[] matrix = transformValues();
        return new Vector3(matrix[MATRIX_DIRECTION_X],
                matrix[MATRIX_DIRECTION_Y],
                matrix[MATRIX_DIRECTION_Z]);
    }

    public Vector3 distance(Entity entity)
    {
        return distance(entity.location());
    }

    public Vector3 distance(Vector3 location)
    {
        return location().sub(location);
    }

    public Ray forwardRay()
    {
        return ray(FORWARD_DIRECTION);
    }

    public boolean isVisible(Camera camera)
    {
        return camera.frustum.sphereInFrustum(locationFromCenter(), radius());
    }


    public BoundingBox boundingBox() {
        return modelInstance().calculateBoundingBox(new BoundingBox());
    }

    public float[] transformValues()
    {
        return transform().getValues();
    }

    public void translate(Vector3 translation)
    {
        transform().translate(translation);
    }

    public void translateABS(Vector3 translation)
    {
        transform().trn(translation);
    }


}
