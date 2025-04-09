package com.kw.gdx.utils.ads;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;

public class BannerView extends Group {

    public BannerView(){
        this(pxToDp(300),pxToDp(Configuration.bannerHeight));
    }

    public BannerView(float bannerWidth, float bannerHight) {
        PixmapImage pixmapImage = new PixmapImage((int)bannerWidth,(int)bannerHight);
//        Image image = new Image(pixmapImage.getPixmap());banner.png
        Image image = new Image(Asset.getAsset().getTexture("banner.png"));
//        addActor(image);
        image.setSize(bannerWidth,bannerHight);
        setSize(bannerWidth,bannerHight);
    }

    public static float pxToDp(float dp){
        float min = Math.max(
                Constant.WIDTH/ Gdx.graphics.getWidth()
                ,Constant.HIGHT/Gdx.graphics.getHeight());
        Constant.gameDensity = Gdx.graphics.getDensity()*min;
        float value = (float) (dp * Constant.gameDensity + 0.5F);
        return value;
    }
}
