package com.libGdx.test.asset;

import com.kw.gdx.asset.Asset;
import com.kw.gdx.resource.AssetRes;
import com.kw.gdx.resource.annotation.AssetFlag;
import com.kw.gdx.resource.annotation.SpineResource;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/27 15:01
 */
@AssetFlag
public class SpineResResourceTest extends AssetRes {
    private static SpineResResourceTest instance;

    @SpineResource(isSpine = true)
    public String jiazaiTupic = "spine/jiazai.json";

    public static SpineResResourceTest getInstance(){
        if (instance == null){
            instance = new SpineResResourceTest();
        }
        return instance;
    }
}
