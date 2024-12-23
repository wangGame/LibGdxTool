package com.kw.gdx.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


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
 

    public void dispose(){
        actorCreate = null;
    }
}
