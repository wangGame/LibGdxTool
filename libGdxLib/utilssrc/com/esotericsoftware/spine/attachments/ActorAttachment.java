package com.esotericsoftware.spine.attachments;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorAttachment extends Attachment{

    private Actor actor;
    private boolean flower;
    private float offsetX;
    private float offsetY;
    private float scale = 1f;
    public ActorAttachment(String name) {
        super(name);
    }

    public Actor getActor() {
        return actor;
    }

    public ActorAttachment(ActorAttachment actorAttachment) {
        super(actorAttachment);
        this.actor = actorAttachment.actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public Attachment copy() {
        return new ActorAttachment(this);
    }

    public void setFlower(boolean flower) {
        this.flower = flower;
    }

    public boolean isFlower() {
        return flower;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }
}
