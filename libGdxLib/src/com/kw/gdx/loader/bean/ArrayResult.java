package com.kw.gdx.loader.bean;

import com.badlogic.gdx.utils.Array;

public class ArrayResult<T> {
    public Array<T> array;

    public <R> Array<R> getArray() {
        return (Array<R>) array;
    }
}
