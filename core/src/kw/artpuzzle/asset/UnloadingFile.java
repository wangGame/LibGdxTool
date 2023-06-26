package kw.artpuzzle.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.kw.gdx.asset.Asset;

public class UnloadingFile {
   public static void unloadResource(String path){
      AssetManager localAssetManager = Asset.getAsset().getLocalAssetManager();
      if (localAssetManager.isLoaded(path)) {
         localAssetManager.unload(path);
      }
      Asset.getAsset().disposeTexture(path);
   }
}
