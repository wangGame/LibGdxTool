package com.kw.gdx.textureunpacker;

public interface TextureUnpackerListener {
    void onUnpackFinished(TextureUnpackerResult result);

    void onUnpackFailed(Throwable error);
}
