package kw.artpuzzle.asset;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.resource.annotation.SpineResource;

public class SpineRes {
    private static SpineRes instance;
    @SpineResource(isSpine = true)
    public String saoguang = "spine/saoguang.json";
    @SpineResource(isSpine = true)
    public String BJG = "spine/beijingguang.json";
    @SpineResource(isSpine = true)
    public String setting = "spine/1_3_setting.json";
    @SpineResource(isSpine = true)
    public String loading = "spine/2_0_loading.json";
    @SpineResource(isSpine = true)
    public String beijingyinyue = "spine/1_3_setting.json";
    @SpineResource(isSpine = true)
    public String dianjishengyin = "spine/dianjishengyin.json";
    @SpineResource(isSpine = true)
    public String jiesuo = "spine/jiesuo.json";
    @SpineResource(isSpine = true)
    public String kaiqiguanbi = "spine/kaiqiguanbi.json";
    @SpineResource(isSpine = true)
    public String lizi = "spine/lizi.json";
    @SpineResource(isSpine = true)
    public String zhendong = "spine/zhendong.json";
    @SpineResource(isSpine = false)
    public String liz1 = "lizi/1";
    @SpineResource(isSpine = false)
    public String liz2 = "lizi/2";
    @SpineResource(isSpine = true)
    public String jiazaiTupic = "spine/jiazai.json";


    public static SpineRes getInstance(){
        if (instance == null){
            instance = new SpineRes();
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
