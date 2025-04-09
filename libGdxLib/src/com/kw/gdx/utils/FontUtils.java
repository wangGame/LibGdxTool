package com.kw.gdx.utils;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.view.label.Label4;

public class FontUtils {
    public static Label4 labelToLabel4(Label monlabel){
        Label4 label4 = new Label4("",new Label4.LabelStyle(){{
            font = monlabel.getStyle().font;
        }});
        monlabel.getParent().addActor(label4);
        monlabel.remove();
        label4.setPosition(monlabel.getX(Align.center),monlabel.getY(Align.center),Align.center);
        label4.setAlignment(Align.center);
        label4.setName(monlabel.getName());

        return label4;
    }
}
