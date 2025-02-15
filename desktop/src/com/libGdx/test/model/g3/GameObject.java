package com.libGdx.test.model.g3;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;

public abstract class GameObject extends Actor implements Disposable {
    private ModelInstance modelInstance;
    private Model model;
    private ModelBatch modelBatch;
    private Camera


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        batch.end();
        modelBatch.begin(cam);
        modelBatch.render(instances,environment);
        modelBatch.end();
        batch.begin();
    }

    @Override
    public abstract void dispose();
}
