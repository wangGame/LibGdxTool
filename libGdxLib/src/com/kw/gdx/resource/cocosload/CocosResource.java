package com.kw.gdx.resource.cocosload;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.log.NLog;

public class CocosResource {

    public static Group loadFile(String resourcePath){
        return new Group();
    }

    public static void unLoadFile(String path){
        if (path!=null){
            if (Asset.getAsset().getAssetManager().isLoaded(path)){
                NLog.i("%s dispose",path);
                Asset.getAsset().getAssetManager().unload(path);
            }
        }
    }
}
