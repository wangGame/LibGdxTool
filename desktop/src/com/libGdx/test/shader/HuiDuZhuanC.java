package com.libGdx.test.shader;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;

public class HuiDuZhuanC extends BaseGroup{
    Image image;
    public HuiDuZhuanC() {
        super("shader/huidu/wave.vert", "shader/huidu/wave.glsl");
        image = new Image(Asset.getAsset().getTexture("img_1.png"));
        addActor(image);
        image.setPosition(0,0, Align.center);

    }
}
