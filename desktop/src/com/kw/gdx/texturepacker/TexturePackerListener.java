package com.kw.gdx.texturepacker;

public interface TexturePackerListener {
    void onPackFinished(TexturePackerResult result);

    void onPackFailed(Throwable error);
}
