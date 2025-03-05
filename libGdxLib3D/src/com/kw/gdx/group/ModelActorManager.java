package com.kw.gdx.group;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

/**
 * 使用模型的时候继承此类
 *
 * java不支持多继承，如果使用模型的时候  需要加入模型的转换， 将是比较麻烦的操作，  开始想叫group  不如叫manager
 */
public class ModelActorManager extends Group {
    private ModelActor modelActor;
    public ModelActorManager(ModelActor actor){
        this.modelActor = actor;
        addActor(actor);
        setSize(actor.getWidth(),actor.getHeight());
        actor.setPosition(getWidth()/2.f,getHeight()/2.f, Align.center);
    }

    public Vector3 get_lrot() {
        return modelActor.get_lrot();
    }

    public void rotation(float v, float v1, float v2) {
        setRotation(v2);
        modelActor.rotation(v,v1,0);
    }
}
