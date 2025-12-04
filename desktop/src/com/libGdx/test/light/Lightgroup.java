package com.libGdx.test.light;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.shader.BaseGroup;

public class Lightgroup extends BaseGroup {
    public Lightgroup() {
        super("shader/light/wave.vert", "shader/light/wave.glsl");
        Image image = new Image(Asset.getAsset().getTexture("hmbb.jpg"));
        addActor(image);
        image.setPosition(0,0, Align.center);
    }

    @Override
    public void setPar() {
        super.setPar();
        program.setUniformf("width",0.4f);
        program.setUniformf("time",stepTime);
    }

    private float stepTime;
    @Override
    public void act(float delta) {
        super.act(delta);
        stepTime+=delta;
    }
}
