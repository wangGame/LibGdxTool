package com.libGdx.test.model.g3;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.kw.gdx.constant.Constant;
import com.solvitaire.app.D;

public class ModelBatchUtils extends ModelBatch {
    private static ModelBatchUtils modelBatchUtils;
    private static Camera useCamera;
    private Environment environment;//可以包含点光源集合和线光源集合

    public ModelBatchUtils(){
        initLight();
        useCamera = Constant.camera;
    }

    public static ModelBatchUtils getInstance(){
        if (modelBatchUtils == null){
            modelBatchUtils = new ModelBatchUtils();
        }
        return modelBatchUtils;
    }

    public void setUseCamera(Camera useCamera) {
        this.useCamera = useCamera;
    }

    public static void disposeBatch() {
        modelBatchUtils = null;
    }

    public void begin(){
        begin(useCamera);
    }

    public void draw(ModelInstance instances){
        render(instances,environment);
    }


    public void initLight(){
        environment = new Environment();
//        directionalLight.
//        directionalLight.direction
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.59f, 0.59f, 0.59f, 1f));//环境光
//        environment.add(new SpotLight());
//        environment.add(directionalLight);

//        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.4f, 0.4f, 1f));
        DirectionalLight directionalLight = new DirectionalLight().set(0.38f, 0.38f, 0.38f, -0, -0, -100);
        environment.add(directionalLight);

    }
}
