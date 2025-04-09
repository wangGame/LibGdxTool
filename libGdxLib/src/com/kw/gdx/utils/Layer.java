package com.kw.gdx.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.constant.Constant;

public class Layer {
    public static Image getShadow(){
        Pixmap pixmap = new Pixmap(20,20,Pixmap.Format.RGBA8888);
        for (int i = 0; i < 20; i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                pixmap.drawPixel(i,i1, Color.WHITE.toIntBits());
            }
        }
        Image shadow= new Image(new Texture(pixmap));

        shadow.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        return shadow;
    }
}