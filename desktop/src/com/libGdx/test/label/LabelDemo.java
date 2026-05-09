package com.libGdx.test.label;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelDemo extends Group {
    public LabelDemo(){
        Label label = new Label("[#FF0000FF]1[]",new Label.LabelStyle(){
            {
                font = new BitmapFont(Gdx.files.internal("assets/font/Cali_75.fnt"));
//                font = Asset.getAsset().getN_R_90_1();
                font.getData().markupEnabled=true;//开启变色

            }
        });
        addActor(label);
    }
}
