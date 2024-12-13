package com.kw.gdx.center;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.LabelCenter;

public class CenterUtils {
    public static void imgLabel(Group group,float distance){
        Actor img = group.findActor("img");
        Actor label = group.findActor("label");
        float groupWith = img.getWidth() + label.getWidth() + distance;
        group.setWidth(groupWith);
        img.setX(0);
        label.setX(img.getWidth()+distance);
    }
}
