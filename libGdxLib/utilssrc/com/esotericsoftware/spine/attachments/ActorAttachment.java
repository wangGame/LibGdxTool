package com.esotericsoftware.spine.attachments;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorAttachment extends Attachment {

    private Actor actor;

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
}
