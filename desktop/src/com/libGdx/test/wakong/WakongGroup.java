package com.libGdx.test.wakong;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
