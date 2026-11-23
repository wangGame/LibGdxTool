package com.kw.gdx.resource;

import com.kw.gdx.asset.Asset;

public abstract class AssetRes {
    public void loadRes(){
        Asset.getAsset().loadAsset(this);
    }

    public void getRes(){
        Asset.getAsset().getResource(this);
    }
}
