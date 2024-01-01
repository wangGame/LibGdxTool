package com.kw.gdx.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * @Auther jian xian si qi
 * @Date 2024/1/1 22:28
 */
public class ColorUtils {
    public static void setActorColor(Actor actor, Color color) {
        if (actor instanceof Group) {
            for (Actor ca : ((Group) actor).getChildren()) {
                setActorColor(ca, color);
            }
        } else {
            actor.setColor(color);
        }
    }
}
