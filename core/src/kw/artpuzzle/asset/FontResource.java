package kw.artpuzzle.asset;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.resource.annotation.FtResource;

public class FontResource {
    private static FontResource instance;
//    @FtResource("fonts/outline.fnt")
//    public BitmapFont aoutline_30;

    public static FontResource getInstance(){
        if (instance == null){
            instance = new FontResource();
        }
        return instance;
    }


    public void loadRes(){
        Asset.getAsset().loadAsset(this);
    }

    public void getRes(){
        Asset.getAsset().getResource(this);
    }
}
