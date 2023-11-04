package com.libGdx.test.scrollpanel;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/5 18:21
 */
class DemoGroup extends Group {
    private Image image;
    public DemoGroup(){
        image = new Image(Asset.getAsset().getTexture("ad_progress.png"));
        image.setDebug(true);
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
        image.setSize(image.getWidth(),image.getHeight());
        image.setPosition(getWidth()/2,getHeight()/2, Align.center);
    }

    private float xxx;
    public void setOffSetX(float xxx){
        if (this.xxx == xxx) {
            return;
        }
        this.xxx = xxx;
        double v =  1.0 - Math.abs(xxx / 200);
        image.setScale(Math.abs((float) v));

    }
}
