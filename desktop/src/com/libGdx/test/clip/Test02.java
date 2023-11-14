package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

/**
 * @Auther jian xian si qi
 * @Date 2023/11/6 18:16
 */
public class Test02 extends Group {
    private float clipWidth = 0;
    public Test02(Actor actor){
        addActor(actor);
        setSize(actor.getWidth(),actor.getHeight());
        setPosition(actor.getX(Align.center),actor.getY(Align.center), Align.center);
        actor.setPosition(getWidth()/2.0f,getHeight()/2.0f,Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (clipBegin(getX(),getY(),clipWidth,getHeight())) {
            super.draw(batch, parentAlpha);
            batch.flush();
            clipEnd();
        }
    }

    public void setClipWidth(float clipWidth) {
        this.clipWidth = clipWidth;
    }
}
