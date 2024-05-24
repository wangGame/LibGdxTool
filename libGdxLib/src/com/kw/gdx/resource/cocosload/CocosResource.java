package com.kw.gdx.resource.cocosload;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.log.NLog;
import com.ui.ManagerUIEditor;
import com.ui.loader.ManagerUILoader;

public class CocosResource {

    public static Group loadFile(String resourcePath){
        if (resourcePath!=null) {
            if (!Asset.getAsset().getAssetManager().isLoaded(resourcePath)){
                ManagerUILoader.ManagerUIParameter managerUIParameter1 =
                        new ManagerUILoader.ManagerUIParameter("cocos/", Asset.getAsset().getAssetManager());
                Asset.getAsset().getAssetManager().load(resourcePath, ManagerUIEditor.class, managerUIParameter1);
                Asset.getAsset().getAssetManager().finishLoading();
            }
            ManagerUIEditor managerUIEditor = Asset.getAsset().getAssetManager().get(resourcePath);
            return managerUIEditor.createGroup();
        }
        return new Group();
    }

    public static ManagerUIEditor loadFil1e1(String resourcePath){
        if (resourcePath!=null) {
            if (!Asset.getAsset().getAssetManager().isLoaded(resourcePath)){
                ManagerUILoader.ManagerUIParameter managerUIParameter1 =
                        new ManagerUILoader.ManagerUIParameter("cocos/", Asset.getAsset().getAssetManager());
                Asset.getAsset().getAssetManager().load(resourcePath, ManagerUIEditor.class, managerUIParameter1);
                Asset.getAsset().getAssetManager().finishLoading();
            }
            ManagerUIEditor managerUIEditor = Asset.getAsset().getAssetManager().get(resourcePath);
            return managerUIEditor;
        }
        return null;
    }

    public static void loadFile1(String resourcePath){
        if (resourcePath!=null) {
            if (!Asset.getAsset().getAssetManager().isLoaded(resourcePath)) {
                ManagerUILoader.ManagerUIParameter managerUIParameter1 =
                        new ManagerUILoader.ManagerUIParameter("ccs/", Asset.getAsset().getAssetManager());
                Asset.getAsset().getAssetManager().load(resourcePath, ManagerUIEditor.class, managerUIParameter1);
                Asset.getAsset().getAssetManager().finishLoading();
            }
        }
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
