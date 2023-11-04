package com.libGdx.test.shader;

import com.badlogic.gdx.scenes.scene2d.Group;

public class ColorCirGroup extends BaseGroup {
    private float time;
    public ColorCirGroup() {
        super("shader/cir/grayScale.vert","shader/cir/colorcir.glsl");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
    }

    @Override
    public void setPar() {
        super.setPar();
        program.setUniformf("time",time);
    }
}