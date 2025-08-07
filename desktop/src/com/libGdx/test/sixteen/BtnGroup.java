package com.libGdx.test.sixteen;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;

public class BtnGroup extends Group {
    public BtnGroup(String name){
        Image btnBg = new Image(Asset.getAsset().getTexture("pic/btn.png"));
        addActor(btnBg);
        setSize(btnBg.getWidth(),btnBg.getHeight());
        Label btnLabel = new Label(name,new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Manrope-ExtraBold_60_1.fnt");
        }});
        addActor(btnLabel);
        btnLabel.setPosition(getWidth()/2f,getHeight()/2f, Align.center);
        setOrigin(Align.center);
    }
}
