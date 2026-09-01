package com.kw.gdx.group;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;

public class LayoutGroup extends Group {
    private SnapshotArray<Actor> tabeleSort ;
    private float padding = 0;
    private float startX;
    public LayoutGroup() {
        super();
        tabeleSort = new SnapshotArray<>();
    }

    public void add(Actor actor) {
        addActor(actor);
        tabeleSort.add(actor);
    }


    public void layoutChild(){
        startX = 0;
        float widthTemp = 0;
        for (Actor actor : tabeleSort) {
            actor.setPosition(startX, getHeight()/2f, Align.left);
            widthTemp = actor.getWidth();
            startX += widthTemp + padding;
        }
        startX -= padding;
        setWidth(startX);
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }
}
