package com.kw.gdx.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

/**
 * 居中
 */
public class LabelCenter {
    public void setCoinNum(Group coinGroup){
        Label coin_label = coinGroup.findActor("coin_label");
        coin_label.setAlignment(Align.center);
        coin_label.setText("v");

        float prefWidth = coin_label.getPrefWidth();
        Actor coin_bg = coinGroup.findActor("coin_bg");
        float v = prefWidth + 110;
        if (v<206.0f){
            v = 206.0f;
        }
        coin_bg.setWidth(v);
        float x = coin_bg.getX(Align.center);
        coin_label.setX(x+20,Align.center);
        coinGroup.setWidth(v+20);
    }
}
