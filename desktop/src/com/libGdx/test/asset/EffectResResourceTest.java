package com.libGdx.test.asset;

import com.kw.gdx.asset.Asset;
import com.kw.gdx.resource.annotation.SpineResource;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/27 15:01
 */
public class EffectResResourceTest {
    private static EffectResResourceTest instance;

    @SpineResource(isSpine = false)
    public String jiazaiTupic = "lizi/baocaidai";

    public static EffectResResourceTest getInstance(){
        if (instance == null){
            instance = new EffectResResourceTest();
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
