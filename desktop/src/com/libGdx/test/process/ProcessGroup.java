package com.libGdx.test.process;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.tietu.CirGroup;

public class ProcessGroup extends CirGroup {
    public ProcessGroup(){
        Image jbt = new Image(Asset.getAsset().getTexture("assets/jdt.png"));
        addActor(jbt);
        setScale(6);
    }

    @Override
    protected void drawCir() {
        sr.circle(20,20,20);
        sr.box(20,0,0,80,40,0);
        sr.circle(100,20,20);
    }
}
