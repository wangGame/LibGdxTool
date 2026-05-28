package com.libGdx.test.sc;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface Adapter<T> {

    Actor createView();

    void bindView(Actor view, T data, int index);
}