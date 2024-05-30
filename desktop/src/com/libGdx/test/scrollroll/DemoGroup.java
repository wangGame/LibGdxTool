package com.libGdx.test.scrollroll;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

public class DemoGroup extends Group {
    public DemoGroup(){

        setSize(Constant.GAMEWIDTH-100,500);
        Image im = new Image(
                new NinePatch(
                        Asset.getAsset().getTexture("white.png"),
                        5,5,5,5));
        addActor(im);
        im.setSize(getWidth(),getHeight());
        Image ims = new Image(
                new NinePatch(
                        Asset.getAsset().getTexture("white.png"),
                        5,5,5,5));
        addActor(ims);
        ims.setSize(getWidth(),3);
        ims.setColor(Color.BLACK);

        Image imu = new Image(
                new NinePatch(
                        Asset.getAsset().getTexture("white.png"),
                        5,5,5,5));
        addActor(imu);
        imu.setSize(getWidth(),3);
        imu.setColor(Color.BLACK);
        imu.setY(getHeight(),Align.top);
    }

    public void setIndex(int i) {
        Label label = new Label("",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Bahnschrift-Regular_40_1.fnt");
        }});
        addActor(label);
        label.setAlignment(Align.center);
        label.setOrigin(Align.center);
        label.setScale(5);
        label.setPosition(getWidth()/2.0f,getHeight()/2.0f, Align.center);
        label.setText(""+i);
        label.setColor(Color.BLACK);
    }
}
