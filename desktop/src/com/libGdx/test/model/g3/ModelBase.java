package com.libGdx.test.model.g3;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

/**
 * 绘制
 */
public abstract class ModelBase extends Actor implements Disposable {
    protected ModelInstance modelInstance;
    private ModelBatchUtils modelBatchUtils;
    public ModelBase(ModelInstance modelInstance){
        this.modelInstance = modelInstance;
        Vector3 vector3 = new Vector3();
        vector3.set(0,0,-400);

        modelBatchUtils = modelBatchUtils.getInstance();
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        batch.end();




        modelBatchUtils.begin();



        modelBatchUtils.render(modelInstance);
        modelBatchUtils.end();

        batch.begin();
    }

    @Override
    public abstract void dispose();


}
