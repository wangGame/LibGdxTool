package com.kw.gdx.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ui.plist.PlistAtlas;

public class ActorCreate {
    private ActorCreate(){

    }

    private static ActorCreate actorCreate;

    public static ActorCreate getInstance(){
        if (actorCreate == null){
            actorCreate = new ActorCreate();
        }
        return actorCreate;
    }

    public Image createImage(String texturePath){
        return new Image(Asset.getAsset().getTexture(texturePath));
    }

    public Image createImage(String plistPath,String texturePath,boolean isInLocal){
        AssetManager assetManager = Asset.getAsset().getAssetManager();
        PlistAtlas plistAtlas;
        if (isInLocal){
            plistAtlas = Asset.getAsset().getPlist(plistPath,Asset.getAsset().getLocalAssetManager());
        }else {
            plistAtlas = Asset.getAsset().getPlist(plistPath,Asset.getAsset().getAssetManager());
        }
        TextureAtlas.AtlasRegion region = plistAtlas.findRegion(texturePath);
        return new Image(region);
    }


    public void dispose(){
        actorCreate = null;
    }
}
