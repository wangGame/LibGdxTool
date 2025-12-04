package com.libGdx.test.cir;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.shader.BaseGroup;

public class CirActor extends BaseGroup {
    public CirActor() {
        super("shader/cirActor/wave.vert", "shader/cirActor/wave.glsl");
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
        image.setPosition(0,0, Align.center);
    }

    @Override
    public void setPar() {
        super.setPar();
        program.setUniformf("wh_ratio",getWidth()/getHeight());
    }
}
