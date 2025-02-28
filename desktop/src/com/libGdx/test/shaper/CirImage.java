package com.libGdx.test.shaper;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class CirImage extends CirGroup{
    public CirImage(){
        Image image = new Image(Asset.getAsset().getTexture("124/frm_red.png"));
        addActor(image);
    }
}
