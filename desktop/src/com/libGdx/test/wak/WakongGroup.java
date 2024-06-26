package com.libGdx.test.wak;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.tietu.CirGroup;

public class WakongGroup extends CirGroup {
    public WakongGroup(){
        Image image = new Image(Asset.getAsset().getTexture("assets/000.png"));
        addActor(image);
    }

    @Override
    protected void drawCir() {
        super.drawCir();
        sr.circle(250,250,83);
    }
}
