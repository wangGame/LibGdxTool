package com.kw.gdx.btn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.listener.OrdinaryButtonListener;

public class BtnUtils {
    public static Group createBtn(Texture texture, Label label, Runnable r){
        Image btn = new Image(texture);
        Group group = new Group();
        group.setSize(btn.getWidth(),btn.getHeight());
        group.addActor(btn);
        group.addActor(label);
        label.setAlignment(Align.center);
        label.setPosition(group.getWidth()/2.0f,group.getHeight()/2.0f,Align.center);
        label.setColor(Color.BROWN);
        group.setOrigin(Align.center);
        group.addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                r.run();
            }
        });
        return group;
    }
}
