package com.libGdx.test.sixteen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TipBg extends Actor {
    TipBgPatch patch;
    private float leftWp;

    public TipBg(TextureRegion region,float left,float leftCenter,float rightCenter,float right,float top,float bottom){
        patch = new TipBgPatch(region, (int) left,(int)leftCenter,(int)rightCenter,(int)right,(int)top,(int)bottom);
    }

    public void setLeftWp(float leftWp) {
        this.leftWp = leftWp;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        if(patch!=null){
            patch.draw(batch,getX(),getY(),leftWp,getWidth(),getHeight());
        }
    }
}
