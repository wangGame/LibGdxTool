package com.libGdx.test.model.g3;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class ModelBatchUtils extends ModelBatch {
    private static ModelBatchUtils modelBatchUtils;
    private Camera useCamera;
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
        setCamera(useCamera);
    }

    public void
}
