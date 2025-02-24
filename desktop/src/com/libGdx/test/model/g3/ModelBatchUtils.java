package com.libGdx.test.model.g3;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.kw.gdx.constant.Constant;

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
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.9f, 0.9f, 0.9f, 1f));//环境光
    }

}
