package com.libGdx.test.blur;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.shader.BaseGroup;

public class BlurGroup extends BaseGroup {
    public BlurGroup() {
        super("shader/blur/wave.vert","shader/blur/wave.glsl");

        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        addActor(image);
    }

}
