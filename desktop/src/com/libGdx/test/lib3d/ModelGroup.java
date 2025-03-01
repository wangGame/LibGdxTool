package com.libGdx.test.lib3d;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.group.ModelActor;
import com.libGdx.test.model.ModelUtils;

public class ModelGroup extends Group {
    private ModelActor actor;
    public ModelGroup(){
        ModelInstance instance = ModelUtils.createInstance(0);
        actor = new ModelActor(instance);
        actor.setDebug(true);
        addActor(actor);
        actor.setPosition(new Vector3(0,0,-200));
        actor.setOrigin(Align.center);
    }
}
