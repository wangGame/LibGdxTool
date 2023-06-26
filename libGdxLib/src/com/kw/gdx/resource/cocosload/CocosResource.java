package com.kw.gdx.resource.cocosload;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.log.NLog;
import com.ui.ManagerUIEditor;
import com.ui.loader.ManagerUILoader;

public class CocosResource {

    public static Group loadFile(String resourcePath){
        if (resourcePath!=null) {
            if (!Asset.getAsset().assetManager.isLoaded(resourcePath)){
                ManagerUILoader.ManagerUIParameter managerUIParameter1 =
                        new ManagerUILoader.ManagerUIParameter("cocos/", Asset.assetManager);
                Asset.assetManager.load(resourcePath, ManagerUIEditor.class, managerUIParameter1);
                Asset.assetManager.finishLoading();
            }
            ManagerUIEditor managerUIEditor = Asset.assetManager.get(resourcePath);
            return managerUIEditor.createGroup();
        }
        return new Group();
    }

    public static void loadFile1(String resourcePath){
        if (resourcePath!=null) {
            if (!Asset.getAsset().assetManager.isLoaded(resourcePath)) {
                ManagerUILoader.ManagerUIParameter managerUIParameter1 =
                        new ManagerUILoader.ManagerUIParameter("ccs/", Asset.assetManager);
                Asset.assetManager.load(resourcePath, ManagerUIEditor.class, managerUIParameter1);
                Asset.assetManager.finishLoading();
            }
        }
    }

    public static void unLoadFile(String path){
        if (path!=null){
            if (Asset.assetManager.isLoaded(path)){
                NLog.i("%s dispose",path);
                Asset.assetManager.unload(path);
            }
        }
    }
}
