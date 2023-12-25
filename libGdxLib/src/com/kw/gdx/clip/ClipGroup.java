package com.kw.gdx.clip;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/21 14:25
 */
public abstract class ClipGroup extends Group {
    protected ClippingAttachment clippingAttachment;
    private SkeletonClipping clipper;

    public ClipGroup(){
        clipper = new SkeletonClipping();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (clippingAttachment != null) {
            clippingAttachment.setClipX(getX());
            clippingAttachment.setClipY(getY());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        clipper.start(clippingAttachment);
        for (Actor child : getChildren()) {
            if (child instanceof TextureRegionActor){
                ((TextureRegionActor)child).draw(batch,clipper,parentAlpha);
            }
        }
        clipper.clipEnd();
    }
}
