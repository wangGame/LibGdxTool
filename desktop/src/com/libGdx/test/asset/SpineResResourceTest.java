package com.libGdx.test.asset;

import com.kw.gdx.asset.Asset;
import com.kw.gdx.resource.annotation.SpineResource;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/27 15:01
 */
public class SpineResResourceTest {
    private static SpineResResourceTest instance;

    @SpineResource(isSpine = true)
    public String jiazaiTupic = "spine/jiazai.json";

    public static SpineResResourceTest getInstance(){
        if (instance == null){
            instance = new SpineResResourceTest();
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
